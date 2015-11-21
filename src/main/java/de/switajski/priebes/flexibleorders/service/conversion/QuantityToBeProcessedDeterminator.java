package de.switajski.priebes.flexibleorders.service.conversion;

import static de.switajski.priebes.flexibleorders.service.QuantityUtility.sumQty;

import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

public class QuantityToBeProcessedDeterminator {

    private ReportItem ri;
    DeliveryHistory history;

    public QuantityToBeProcessedDeterminator(ReportItem reportItem) {
        ri = reportItem;
        history = DeliveryHistory.of(ri.getOrderItem());
    }

    public int overdueQuantity() {
        if (ri instanceof ConfirmationItem) {
            if (!((ConfirmationItem) ri).isAgreed()) {
                return ri.getQuantity();
            }
            else {
                return overdueQuantity(ConfirmationItem.class, ShippingItem.class);
            }
        }
        else if (ri instanceof ShippingItem) return overdueQuantity(ShippingItem.class, InvoiceItem.class);
        else if (ri instanceof InvoiceItem) return overdueQuantity(InvoiceItem.class, ReceiptItem.class);
        else return 0;
    }

    private int overdueQuantity(Class<? extends ReportItem> currentType, Class<? extends ReportItem> typeToBeCreated) {
        DeliveryHistory history = DeliveryHistory.of(ri.getOrderItem());

        Set<ReportItem> matchingInvoiceItems = new HashSet<ReportItem>();
        for (ReportItem item : history.reportItems(typeToBeCreated)) {
            if (item.getQuantity() == ri.getQuantity()) matchingInvoiceItems.add(item);
        }
        Set<ReportItem> concurringShippingItems = new HashSet<ReportItem>();
        for (ReportItem item : history.reportItems(currentType)) {
            if (item.getQuantity() == ri.getQuantity()) concurringShippingItems.add(item);
        }

        if (matchingInvoiceItems.size() > 0) {
            if (concurringShippingItems.size() == 1) return 0;
            ReportItem resolved =
                    new ConcurringItemResolver(matchingInvoiceItems, concurringShippingItems)
                            .resolve(ri);
            if (resolved == null) {
                return ri.getQuantity();
            }
            else {
                return 0;
            }

        }
        else {
            return ri.getQuantity();
        }

    }

    private int toBeProcesse() {
        return sumQty(history.reportItems(InvoiceItem.class)) - sumQty(history.reportItems(ReceiptItem.class));
    }

}
