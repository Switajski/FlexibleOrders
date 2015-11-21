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

    public int toBeProcessed() {
        if (ri instanceof ConfirmationItem) {
            if (!((ConfirmationItem) ri).isAgreed()) {
                return toBeAgreed();
            }
            else {
                return toBeShipped();
            }
        }
        else if (ri instanceof ShippingItem) return toBeInvoiced();
        else if (ri instanceof InvoiceItem) return toBePaid();
        else return 0;
    }

    private Integer toBeAgreed() {
        return sumQty(history.notAgreedConfirmationItems()) - sumQty(history.agreedConfirmationItems());
    }

    private int toBeShipped() {
        return sumQty(history.agreedConfirmationItems()) - sumQty(history.reportItems(ShippingItem.class));
    }

    // TODO rename to is overdue and overdueQty - remove
    // reportItem.toBeProcessed, because only needed when creating overdue items
    private int toBeInvoiced() {
        DeliveryHistory history = DeliveryHistory.of(ri.getOrderItem());

        Set<ReportItem> matchingInvoiceItems = new HashSet<ReportItem>();
        for (ReportItem item : history.reportItems(InvoiceItem.class)) {
            if (item.getQuantity() == ri.getQuantity()) matchingInvoiceItems.add(item);
        }
        Set<ReportItem> concurringShippingItems = new HashSet<ReportItem>();
        for (ReportItem item : history.reportItems(ShippingItem.class)) {
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

    private int toBePaid() {
        return sumQty(history.reportItems(InvoiceItem.class)) - sumQty(history.reportItems(ReceiptItem.class));
    }

}
