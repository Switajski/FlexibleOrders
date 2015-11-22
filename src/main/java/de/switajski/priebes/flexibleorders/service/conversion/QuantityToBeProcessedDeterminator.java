package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.service.QuantityUtility;

@Transactional(readOnly = true)
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
        int quantity = ri.getQuantity();

        Set<ReportItem> matchingInvoiceItems = qtyMatches(typeToBeCreated, quantity);
        Set<ReportItem> concurringShippingItems = qtyMatches(currentType, quantity);

        if (matchingInvoiceItems.size() > 0) {
            if (concurringShippingItems.size() == 1) return 0;
            ReportItem resolved =
                    new ConcurringItemResolver(matchingInvoiceItems, concurringShippingItems)
                            .resolve(ri);
            if (resolved == null) {
                return quantity;
            }
            else {
                return 0;
            }

        }
        else {
            int difference = quantity - QuantityUtility.sumQty(history.reportItems(typeToBeCreated));
            if (difference > 0) return difference;
            else return quantity;
        }

    }

    private Set<ReportItem> qtyMatches(Class<? extends ReportItem> typeToBeCreated, int quantity) {
        Set<ReportItem> matchingInvoiceItems = new HashSet<ReportItem>();
        for (ReportItem item : history.reportItems(typeToBeCreated)) {
            if (item.getQuantity() == quantity) matchingInvoiceItems.add(item);
        }
        return matchingInvoiceItems;
    }

}
