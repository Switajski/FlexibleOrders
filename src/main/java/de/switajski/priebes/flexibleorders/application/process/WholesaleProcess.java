package de.switajski.priebes.flexibleorders.application.process;

import java.util.ArrayList;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Receipt;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

/**
 *
 * @author Marek Switajski
 *
 */
public class WholesaleProcess {

    public static List<Class<? extends ReportItem>> reportItemSteps() {
        List<Class<? extends ReportItem>> reportItemOrder = new ArrayList<Class<? extends ReportItem>>();
        reportItemOrder.add(ConfirmationItem.class);
        reportItemOrder.add(ShippingItem.class);
        reportItemOrder.add(InvoiceItem.class);
        reportItemOrder.add(ReceiptItem.class);
        return reportItemOrder;
    }

    public static List<Class<? extends Report>> reportSteps() {
        List<Class<? extends Report>> reportOrder = new ArrayList<Class<? extends Report>>();
        reportOrder.add(OrderConfirmation.class);
        reportOrder.add(DeliveryNotes.class);
        reportOrder.add(Invoice.class);
        reportOrder.add(Receipt.class);
        return reportOrder;
    }

}
