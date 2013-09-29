package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import de.switajski.priebes.flexibleorders.component.ItemTransition;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.domain.parameter.ConfirmationParameter;

public class ShippingItemBuilder implements Builder<ShippingItem> {

	//Dependencies
	ConfirmationParameter confirmationParameter;
	
	//Logical dependencies
	OrderItem orderItem;
	
	public ShippingItemBuilder() {
		OrderItemBuilder oib = new OrderItemBuilder();
		orderItem = oib.build();
		
	}
	
	public ShippingItemBuilder withConfirmationParameter(ConfirmationParameter confirmationParameter){
		this.confirmationParameter = confirmationParameter;
		return this;
	}
	
	public ShippingItemBuilder withOrderItem(OrderItem orderItem){
		this.orderItem = orderItem;
		return this;
	}
	
	@Override
	public ShippingItem build() {
		if (orderItem == null)
			orderItem = new OrderItemBuilder().build();
		if (confirmationParameter == null)
			confirmationParameter = new ConfirmationParameter(false, 1l);
		
		ShippingItem si = new ItemTransition().confirm(orderItem, confirmationParameter);
		return si;
	}

}
