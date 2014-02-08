package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.Currency;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OriginSystem;
import de.switajski.priebes.flexibleorders.domain.Product;

public class ItemBuilder implements Builder<OrderItem> {

	private Set<HandlingEvent> deliveryHistory = new HashSet<HandlingEvent>();
	private Integer orderedQuantity;
	private Amount negotiatedPriceNet;
	private Product product;
	private String packageNumber;
	private String trackingNumber;
	private FlexibleOrder order;
	
	public ItemBuilder(FlexibleOrder order, Product product, int orderedQuantity) {
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
	
	public ItemBuilder generateAttributes(Integer i){
		orderedQuantity = i;
		negotiatedPriceNet = new Amount(new BigDecimal(i), Currency.EUR);
		packageNumber = i.toString();
		trackingNumber = i.toString();
		return this;
	}
	
	public static OrderItem buildWithGeneratedAttributes(Integer i){
		return new ItemBuilder(
				new FlexibleOrder(
					"", 
					OriginSystem.FLEXIBLE_ORDERS, 
					i.toString()), 
				CatalogProductBuilder.buildWithGeneratedAttributes(i).toProduct(), 
					i)
		.generateAttributes(i).build();
	}

	public ItemBuilder setDeliveryHistory(Set<HandlingEvent> deliveryHistory) {
		this.deliveryHistory = deliveryHistory;
		return this;
	}

	public ItemBuilder setOrderedQuantity(Integer orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
		return this;
	}

	public ItemBuilder setNegotiatedPriceNet(Amount negotiatedPriceNet) {
		this.negotiatedPriceNet = negotiatedPriceNet;
		return this;
	}

	public ItemBuilder setProduct(Product product) {
		this.product = product;
		return this;
	}

	public ItemBuilder setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
		return this;
	}

	public ItemBuilder setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
		return this;
	}

	public ItemBuilder setOrder(FlexibleOrder order) {
		this.order = order;
		return this;
	}
	
	public ItemBuilder addHandlingEvent(HandlingEvent handlingEvent){
		this.deliveryHistory.add(handlingEvent);
		return this;
	}

}
