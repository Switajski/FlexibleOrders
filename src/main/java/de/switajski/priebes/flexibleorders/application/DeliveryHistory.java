package de.switajski.priebes.flexibleorders.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.switajski.priebes.flexibleorders.application.process.WholesaleProcess;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

/**
 * Concept is to provide delivery histories without a call another service.
 * Therefore the client has to retrieve itself all relevant report items.
 *
 * @author Marek
 *
 */
public class DeliveryHistory {

    private Collection<ReportItem> reportItems;

    public DeliveryHistory(Collection<ReportItem> reportItems) {
        this.reportItems = Collections.unmodifiableCollection(reportItems);
    }

    /**
     * Creates whole delivery history
     *
     * @param reportItem
     * @return
     */
    public static DeliveryHistory of(ReportItem reportItem) {
        return new DeliveryHistory(reportItem.getOrderItem().getReportItems());
    }

    public static DeliveryHistory of(OrderItem orderItem) {
        return new DeliveryHistory(orderItem.getReportItems());
    }

    public <T extends ReportItem> Set<T> reportItems(Class<T> type) {
        Set<T> riToReturn = new HashSet<T>();
        for (ReportItem ri : reportItems) {
            if (type.isInstance(ri)) riToReturn.add(type.cast(ri));
        }
        return riToReturn;
    }

    public <T extends Report> Set<T> getReports(Class<T> clazz) {
        Set<T> reports = new HashSet<T>();
        for (ReportItem ri : reportItems) {
            if (clazz.isInstance(ri.getReport())) reports.add(clazz.cast(ri.getReport()));
        }
        return reports;
    }

    public Set<ReportItem> getItems() {
        return new HashSet<ReportItem>(reportItems);
    }

    public List<ReportItem> getItemsSorted() {
        List<ReportItem> ris = new ArrayList<ReportItem>(getItems());
        Collections.sort(ris, new Comparator<ReportItem>() {
            @Override
            public int compare(ReportItem o1, ReportItem o2) {
                return o1.getCreated().compareTo(o2.getCreated());
            }
        });
        return ris;
    }

    public boolean isEmpty() {
        return reportItems.isEmpty();
    }

    public Set<String> relatedOrderNumbers() {
        Set<String> orders = new HashSet<String>();
        for (ReportItem ri : reportItems)
            orders.add(ri.getOrderItem().getOrder().getOrderNumber());
        return orders;

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
            for (ReportItem item : reportItems(type)) {
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
