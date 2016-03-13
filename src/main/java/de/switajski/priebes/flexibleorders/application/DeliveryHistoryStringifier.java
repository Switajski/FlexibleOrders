package de.switajski.priebes.flexibleorders.application;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import de.switajski.priebes.flexibleorders.application.process.WholesaleProcess;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

/**
 * @author Marek
 *
 */
public class DeliveryHistoryStringifier {

    private Collection<ReportItem> reportItems;

    public DeliveryHistoryStringifier(Collection<ReportItem> reportItems) {
        this.reportItems = Collections.unmodifiableCollection(reportItems);
    }

    private <T extends ReportItem> Set<T> filter(Class<T> type) {
        return reportItems.stream()
                .filter(p -> type.isInstance(p))
                .map(ri -> type.cast(ri))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Orderno: ");
        new UniqueStringOfReportItemsCollectorTemplate() {
            @Override
            public String createString(ReportItem ri) {
                return new StringBuilder().append(ri.getOrderItem().getOrder().getOrderNumber())
                        .append(" ")
                        .append(ri.getOrderItem().getOrderedQuantity())
                        .append(" x ")
                        .append(ri.getOrderItem().getProduct().getProductNumber())
                        .append(" ")
                        .append(ri.getOrderItem().getProduct().getName())
                        .toString();
            }
        }.run(reportItems, s);
        s.append("\n-------------------------------------");
        for (Class<? extends ReportItem> type : WholesaleProcess.reportItemSteps()) {
            s.append("\n" + type.getSimpleName() + "s: ");
            for (ReportItem item : filter(type)) {
                if (item instanceof ConfirmationItem) {
                    if (((ConfirmationItem) item).isAgreed()) {
                        s.append(" - agreed - ");
                    }
                    else {
                        s.append(" - not agreed - ");
                    }
                }
                s.append("\n     " + item);
            }
        }
        return s.toString();
    }

}
