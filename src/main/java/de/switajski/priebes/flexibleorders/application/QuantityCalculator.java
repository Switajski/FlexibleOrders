package de.switajski.priebes.flexibleorders.application;

import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

public class QuantityCalculator {

	public static Integer calculate(ReportItem reportItem) {
		DeliveryHistory history = DeliveryHistory.createFrom(reportItem);
		if (reportItem instanceof AgreementItem)
			return toBeAgreed(history);
		else if (reportItem instanceof ConfirmationItem)
			return toBeShipped(history);
		else if (reportItem instanceof ShippingItem)
			return toBeInvoiced(history);
		else if (reportItem instanceof InvoiceItem)
			return toBePaid(history);
		else return 0;
	}
	
	public static Integer toBeAgreed(DeliveryHistory history) {
		return sumQty(history.getItems(ConfirmationItem.class)) - sumQty(history.getItems(AgreementItem.class));
	}

	public static int toBeInvoiced(DeliveryHistory history){
		return sumQty(history.getItems(ShippingItem.class)) - sumQty(history.getItems(InvoiceItem.class)); 
	}
	
	public static int toBeShipped(DeliveryHistory history){
		return sumQty(history.getItems(AgreementItem.class)) - sumQty(history.getItems(ShippingItem.class)); 
	}
	
	public static int toBePaid(DeliveryHistory history){
		return sumQty(history.getItems(InvoiceItem.class)) - sumQty(history.getItems(ReceiptItem.class)); 
	}
	
	public static int toBeConfirmed(OrderItem orderItem) {
		DeliveryHistory deliveryHistory = DeliveryHistory.createFrom(orderItem);
		if (deliveryHistory.isEmpty())
			return orderItem.getOrderedQuantity();
		return orderItem.getOrderedQuantity() - sumQty(deliveryHistory.getItems(ConfirmationItem.class));
	}
	
	private static Integer sumQty(Set<? extends ReportItem> reportItems) {
		int summed = 0;
		for (ReportItem ri : reportItems) {
			summed = summed + ri.getQuantity();
		}
		return summed;
	}

}
