package de.switajski.priebes.flexibleorders.report;

import java.util.List;

import de.switajski.priebes.flexibleorders.domain.ShippingItem;

public class OrderConfirmation extends Report<ShippingItem> {

	public OrderConfirmation(List<ShippingItem> items) {
		super(items);
	}

}
