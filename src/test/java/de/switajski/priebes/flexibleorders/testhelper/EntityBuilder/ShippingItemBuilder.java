package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

public class ShippingItemBuilder extends ReportItemBuilder<ShippingItemBuilder> implements Builder<ShippingItem>{

	public ShippingItem build() {
		ShippingItem ii = new ShippingItem((DeliveryNotes) report, item, quantity, date);
		ii.setQuantity(quantity);
		ii.setReport((DeliveryNotes) report);
		return ii;
	}
}