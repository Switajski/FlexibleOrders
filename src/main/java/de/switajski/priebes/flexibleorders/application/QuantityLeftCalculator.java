package de.switajski.priebes.flexibleorders.application;

import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

public class QuantityLeftCalculator {

	public Integer calculate(ReportItem reportItem) {
		DeliveryHistory history = reportItem.getOrderItem().getDeliveryHistory();
		if (reportItem instanceof ConfirmationItem)
			return toBeShipped(history);
		else if (reportItem instanceof ShippingItem)
			return toBeInvoiced(history);
		else if (reportItem instanceof InvoiceItem)
			return toBePaid(history);
		else return 0;
	}

	public int toBeInvoiced(DeliveryHistory history){
		return sumQty(history.getShippingItems()) - sumQty(history.getInvoiceItems()); 
	}
	
	public int toBeShipped(DeliveryHistory history){
		return sumQty(history.getConfirmationItems()) - sumQty(history.getShippingItems()); 
	}
	
	public int toBeConfirmed(OrderItem orderItem) {
		DeliveryHistory deliveryHistory = orderItem.getDeliveryHistory();
		if (deliveryHistory.isEmpty())
			return orderItem.getOrderedQuantity();
		return orderItem.getOrderedQuantity() - sumQty(deliveryHistory.getConfirmationItems());
	}
	
	public int toBePaid(DeliveryHistory history){
		return sumQty(history.getInvoiceItems()) - sumQty(history.getReceiptItems()); 
	}
	
	private Integer sumQty(Set<? extends ReportItem> reportItems) {
		int summed = 0;
		for (ReportItem ri : reportItems) {
			summed = summed + ri.getQuantity();
		}
		return summed;
	}

}
