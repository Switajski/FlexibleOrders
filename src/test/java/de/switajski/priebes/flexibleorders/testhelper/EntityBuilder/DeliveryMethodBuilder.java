package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.DeliveryType;

public class DeliveryMethodBuilder implements Builder<DeliveryMethod> {

	private String name, phone;

	private Long id;

	private Address address;
	
	private DeliveryType deliveryType;

	@Override
	public DeliveryMethod build() {
		DeliveryMethod c = new DeliveryMethod();
		c.setName(name);
		c.setPhone(phone);
		c.setId(id);
		c.setAddress(address);
		c.setDeliveryType(deliveryType);
		return c;
	}

	public DeliveryMethodBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public DeliveryMethodBuilder setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public DeliveryMethodBuilder setId(Long id) {
		this.id = id;
		return this;
	}

	public DeliveryMethodBuilder setAddress(Address address) {
		this.address = address;
		return this;
	}
	
	public DeliveryMethodBuilder setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
		return this;
	}

	public DeliveryMethodBuilder ups() {
		setId(1L)
		.setAddress(new Address(
				"United Parcel Service Detschland Inc. & Co. OHG",
				null,
				"Görlitzer Straße 1",
				41460,
				"Neuss",
				Country.DEUTSCHLAND))
		.setDeliveryType(DeliveryType.SPEDITION)
		.setPhone("01806 882 663")
		.setName("United Pacel Service");
		return this;
	}
	
	public DeliveryMethodBuilder dhl() {
		setId(2L)
		.setAddress(new Address(
				"Deutsche Post AG",
				null,
				"Charles-de-Gaulle-Str. 20",
				53113,
				"Bonn",
				Country.DEUTSCHLAND))
		.setDeliveryType(DeliveryType.SPEDITION)
		.setPhone("+49 (0)228 182-0")
		.setName("Deutsche Post AG");
		return this;
	}

}
