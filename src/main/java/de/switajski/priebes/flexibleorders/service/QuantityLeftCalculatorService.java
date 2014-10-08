package de.switajski.priebes.flexibleorders.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

// TODO: this class has become a magnet of dependencies! bring the methods back to entities. This would be also a measure against the anemic domain model
@Service
public class QuantityLeftCalculatorService {

    // TODO: refactor this - too many dependencies
    public int calculateLeft(ReportItem reportItem) {
        DeliveryHistory history = DeliveryHistory.of(reportItem);
        if (reportItem instanceof ConfirmationItem) {
            if (!((ConfirmationItem) reportItem).isAgreed()) {
                return toBeAgreed(history, ConfirmationItem.class);
            }
            else {
                return toBeShipped(history);
            }
        }
        else if (reportItem instanceof ShippingItem) return toBeInvoiced(history);
        else if (reportItem instanceof InvoiceItem) return toBePaid(history);
        else return 0;
    }

    public int calculateLeft(OrderItem orderItem) {
        DeliveryHistory deliveryHistory = DeliveryHistory.of(orderItem);
        if (deliveryHistory.isEmpty()) {
            return orderItem.getOrderedQuantity();
        }
        return orderItem.getOrderedQuantity() - sumQty(deliveryHistory.getAgreedConfirmationItems());
    }

    public Integer toBeAgreed(DeliveryHistory history, Class<? extends ReportItem> clazz) {
        return sumQty(history.getNonAgreedConfirmationItems()) - sumQty(history.getAgreedConfirmationItems());
    }

    public int toBeShipped(DeliveryHistory history) {
        return sumQty(history.getAgreedConfirmationItems()) - sumQty(history.getReportItems(ShippingItem.class));
    }

    public int toBeInvoiced(DeliveryHistory history) {
        return sumQty(history.getReportItems(ShippingItem.class)) - sumQty(history.getReportItems(InvoiceItem.class));
    }

    public int toBePaid(DeliveryHistory history) {
        return sumQty(history.getReportItems(InvoiceItem.class)) - sumQty(history.getReportItems(ReceiptItem.class));
    }

    private Integer sumQty(Set<? extends ReportItem> reportItems) {
        int summed = 0;
        for (ReportItem ri : reportItems) {
            summed = summed + ri.getQuantity();
        }
        return summed;
    }

    /**
     * 
     * @param qty to bring in the next process step
     * @param reportItem should have enough 
     */
    public void validateQuantity(Integer qty, ReportItem reportItem) {
        if (qty == null) throw new IllegalArgumentException("Menge nicht angegeben");
        if (calculateLeft(reportItem) == 0) throw new IllegalArgumentException(
                reportItem.toString() + " hat keine offenen Positionen mehr");
        if (qty < 1) throw new IllegalArgumentException("Menge kleiner eins");
        if (qty > calculateLeft(reportItem)) throw new IllegalArgumentException(
                "angeforderte Menge ist zu gross");
    }
}
