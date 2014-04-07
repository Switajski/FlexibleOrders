package de.switajski.priebes.flexibleorders.domain.factory;

import java.util.Date;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.policy.AllowAllOrderPolicy;
import de.switajski.priebes.flexibleorders.policy.OrderPolicy;

/**
 * Handling for wholesale order process implementation.
 *  
 * @author Marek Switajski
 *
 */
public class WholesaleOrderHandlingFactory {
	
	private static OrderPolicy orderPolicy = new AllowAllOrderPolicy();
	
	/**
	 * Adds an OrderItem to given order.
	 * @param order
	 * @param product
	 * @param orderedQuantity
	 * @param priceNet 
	 * @return
	 */
	public OrderItem addOrderItem(Order order, Product product,
			int orderedQuantity, Amount priceNet) {
		if (orderPolicy.isAllowed(order.getCustomer())){
			OrderItem item = new OrderItem(order, product, orderedQuantity);
			item.setOrder(order);
			item.setNegotiatedPriceNet(priceNet);
			return item;
		}
		return null;
	}
	
	public OrderItem addToOrderConfirmation(Report orderConfirmation, OrderItem itemToConfirm, 
			int quantity, Amount negotiatedPriceNet, Date created){
		if (created == null) created = new Date();
		ReportItem he = new ReportItem(orderConfirmation, ReportItemType.CONFIRM,  
				itemToConfirm, quantity, created);
		itemToConfirm.addHandlingEvent(he);
		itemToConfirm.setNegotiatedPriceNet(negotiatedPriceNet);
		return itemToConfirm;
	}
	
	public OrderItem createOrderConfirmation(OrderItem itemToConfirm, int quantity, 
			Amount negotiatedPriceNet, Date created, ConfirmationReport orderConfirmation,
			Date expectedDelivery){
		if (created == null) created = new Date();
		
		ReportItem he = 
				new ReportItem(orderConfirmation, ReportItemType.CONFIRM, itemToConfirm, quantity, created);
		itemToConfirm.setNegotiatedPriceNet(negotiatedPriceNet);
		itemToConfirm.addHandlingEvent(he);
		return itemToConfirm;
	}

	public OrderItem createDeliveryNotes(OrderItem shippingItemToDeliver, DeliveryNotes deliveryNotes, 
			Integer quantity, Date created) {
		if (created == null) created = new Date();
		
		ReportItem he = new ReportItem(deliveryNotes, ReportItemType.SHIP, shippingItemToDeliver, quantity, created);
		shippingItemToDeliver.addHandlingEvent(he);
		return shippingItemToDeliver;
	}
	
	public OrderItem addToInvoice(Report invoice, OrderItem shippingItemToDeliver, int quantity,
			String invoiceNo, Date created) {
		if (created == null) created = new Date();
		ReportItem he = new ReportItem(invoice, ReportItemType.SHIP ,shippingItemToDeliver, quantity, created);
		shippingItemToDeliver.addHandlingEvent(he);
		return shippingItemToDeliver;
	}
	
}
