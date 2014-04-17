package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.Currency;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OriginSystem;
import de.switajski.priebes.flexibleorders.domain.Product;

public class OrderItemBuilder implements Builder<OrderItem> {

	private Set<ReportItem> deliveryHistory = new HashSet<ReportItem>();
	private Integer orderedQuantity;
	private Amount negotiatedPriceNet;
	private Product product;
	private String packageNumber;
	private String trackingNumber;
	private Order order;
	
	public OrderItemBuilder(Order order, Product product, int orderedQuantity) {
		this.order = order;
		this.product = product;
		this.orderedQuantity = orderedQuantity;
	}
	
	@Override
	public OrderItem build() {
		OrderItem item = new OrderItem(order, product, orderedQuantity);
		item.setDeliveryHistory(deliveryHistory);
		item.setNegotiatedPriceNet(negotiatedPriceNet);
		item.setPackageNumber(packageNumber);
		item.setTrackingNumber(trackingNumber);
		return item;
	}
	
	public OrderItemBuilder generateAttributes(Integer i){
		orderedQuantity = i;
		negotiatedPriceNet = new Amount(new BigDecimal(i), Currency.EUR);
		packageNumber = i.toString();
		trackingNumber = i.toString();
		return this;
	}
	
	public static OrderItem buildWithGeneratedAttributes(Integer i){
		return new OrderItemBuilder(
				new Order(
					"", 
					OriginSystem.FLEXIBLE_ORDERS, 
					i.toString()), 
				CatalogProductBuilder.buildWithGeneratedAttributes(i).toProduct(), 
					i)
		.generateAttributes(i).build();
	}

	public OrderItemBuilder setDeliveryHistory(Set<ReportItem> deliveryHistory) {
		this.deliveryHistory = deliveryHistory;
		return this;
	}

	public OrderItemBuilder setOrderedQuantity(Integer orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
		return this;
	}

	public OrderItemBuilder setNegotiatedPriceNet(Amount negotiatedPriceNet) {
		this.negotiatedPriceNet = negotiatedPriceNet;
		return this;
	}

	public OrderItemBuilder setProduct(Product product) {
		this.product = product;
		return this;
	}

	public OrderItemBuilder setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
		return this;
	}

	public OrderItemBuilder setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
		return this;
	}

	public OrderItemBuilder setOrder(Order order) {
		this.order = order;
		return this;
	}
	
	public OrderItemBuilder addHandlingEvent(ReportItem handlingEvent){
		this.deliveryHistory.add(handlingEvent);
		return this;
	}

}
