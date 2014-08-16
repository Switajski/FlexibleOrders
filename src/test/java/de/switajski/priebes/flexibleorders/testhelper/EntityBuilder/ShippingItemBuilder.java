package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

public class ShippingItemBuilder extends ReportItemBuilder<ShippingItem, Builder<ShippingItem>>{

	@Override
	public ShippingItem build() {
		ShippingItem shippingItem = new ShippingItem();
		super.build(shippingItem);
		return shippingItem;
	}

}