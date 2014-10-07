package de.switajski.priebes.flexibleorders.application;

import java.util.Set;

import de.switajski.priebes.flexibleorders.application.process.WholesaleProcessSteps;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

// TODO: this class has become a magnet of dependencies! bring the methods back to entities. This would be also a measure against the anemic domain model
public class QuantityCalculator {

    // TODO: refactor this - too many dependencies
    public static int calculateLeft(ReportItem reportItem) {
        DeliveryHistory history = DeliveryHistory.of(reportItem);
        if (reportItem instanceof ConfirmationItem) return toBeAgreed(history, ConfirmationItem.class);
        else if (reportItem instanceof AgreementItem) return toBeShipped(history);
        else if (reportItem instanceof ShippingItem) return toBeInvoiced(history);
        else if (reportItem instanceof InvoiceItem) return toBePaid(history);
        else return 0;
    }

    public static int calculateLeft(OrderItem orderItem) {
        DeliveryHistory deliveryHistory = DeliveryHistory.of(orderItem);
        if (deliveryHistory.isEmpty()) return orderItem.getOrderedQuantity();
        return orderItem.getOrderedQuantity() - sumQty(deliveryHistory.getReportItems(WholesaleProcessSteps.reportItemSteps().get(0)));
    }

    public static Integer toBeAgreed(DeliveryHistory history, Class<? extends ReportItem> clazz) {
        return sumQty(history.getReportItems(ConfirmationItem.class)) - sumQty(history.getReportItems(AgreementItem.class));
    }

    public static int toBeShipped(DeliveryHistory history) {
        return sumQty(history.getReportItems(AgreementItem.class)) - sumQty(history.getReportItems(ShippingItem.class));
    }

    public static int toBeInvoiced(DeliveryHistory history) {
        return sumQty(history.getReportItems(ShippingItem.class)) - sumQty(history.getReportItems(InvoiceItem.class));
    }

    public static int toBePaid(DeliveryHistory history) {
        return sumQty(history.getReportItems(InvoiceItem.class)) - sumQty(history.getReportItems(ReceiptItem.class));
    }

    private static Integer sumQty(Set<? extends ReportItem> reportItems) {
        int summed = 0;
        for (ReportItem ri : reportItems) {
            summed = summed + ri.getQuantity();
        }
        return summed;
    }

}
