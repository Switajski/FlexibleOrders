package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import de.switajski.priebes.flexibleorders.component.ItemTransition;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.domain.parameter.Address;
import de.switajski.priebes.flexibleorders.domain.parameter.ConfirmationParameter;
import de.switajski.priebes.flexibleorders.domain.parameter.ShippingParameter;

public class InvoiceItemBuilder implements Builder<InvoiceItem> {

	//not null(s)
	Address address;
	
	//Dependencies
	ShippingParameter shippingParameter;
	
	//Dependencies that are not restricted by JPA
	ShippingItem shippingItem;
	
	public InvoiceItemBuilder() {}
	
	public InvoiceItemBuilder withShippingParameter(ShippingParameter shippingParameter){
		this.shippingParameter = shippingParameter;
		return this;
	}
	
	public InvoiceItemBuilder withShippingItem(ShippingItem shippingItem){
		this.shippingItem = shippingItem;
		return this;
	}
	
	@Override
	public InvoiceItem build() {
		if (address == null)
			address = new AddressBuilder().build();
		if (shippingParameter == null)
			shippingParameter = new ShippingParameter(1, 1l, address);
		
		InvoiceItem ii = new ItemTransition().deliver(shippingItem, shippingParameter);
		
		return ii;
	}

}
