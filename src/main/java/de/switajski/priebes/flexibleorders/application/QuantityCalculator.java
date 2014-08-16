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

public class QuantityCalculator {

	/**
	 * Calculates the quantity left to next process step.</br>
	 * </br>
	 * <b>e.g.:</b></br>
	 * If a ConfirmationItem is given, the method calculates how many pieces of the ConfirmationItem have to be agreed.
	 * 
	 * @param reportItem
	 * @return
	 */
	public static int calculateLeft(ReportItem reportItem) {
		DeliveryHistory history = DeliveryHistory.createFrom(reportItem);
		if (reportItem instanceof ConfirmationItem)
			return toBeAgreed(history, ConfirmationItem.class);
		else if (reportItem instanceof AgreementItem)
			return toBeShipped(history);
		else if (reportItem instanceof ShippingItem)
			return toBeInvoiced(history);
		else if (reportItem instanceof InvoiceItem)
			return toBePaid(history);
		else return 0;
	}

	public static int calculateLeft(OrderItem orderItem) {
		DeliveryHistory deliveryHistory = DeliveryHistory.createFrom(orderItem);
		if (deliveryHistory.isEmpty())
			return orderItem.getOrderedQuantity();
		return orderItem.getOrderedQuantity() - sumQty(deliveryHistory.getItems(WholesaleProcessSteps.reportItemSteps().get(0)));
	}
	
	public static Integer toBeAgreed(DeliveryHistory history, Class<? extends ReportItem> clazz) {
//		return sumQty(history.getItems(ConfirmationItem.class)) - sumQty(history.getItems(WholesaleProcessSteps.getNextReportItemStep(ConfirmationItem.class)));
		return sumQty(history.getItems(ConfirmationItem.class)) - sumQty(history.getItems(AgreementItem.class));
	}
	
	public static int toBeShipped(DeliveryHistory history){
		return sumQty(history.getItems(AgreementItem.class)) - sumQty(history.getItems(ShippingItem.class)); 
	}

	public static int toBeInvoiced(DeliveryHistory history){
		return sumQty(history.getItems(ShippingItem.class)) - sumQty(history.getItems(InvoiceItem.class)); 
	}
	
	public static int toBePaid(DeliveryHistory history){
		return sumQty(history.getItems(InvoiceItem.class)) - sumQty(history.getItems(ReceiptItem.class)); 
	}
	
	private static Integer sumQty(Set<? extends ReportItem> reportItems) {
		int summed = 0;
		for (ReportItem ri : reportItems) {
			summed = summed + ri.getQuantity();
		}
		return summed;
	}

}
