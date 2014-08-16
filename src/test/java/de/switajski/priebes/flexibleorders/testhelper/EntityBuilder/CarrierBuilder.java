package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.Carrier;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.reference.Country;

public class CarrierBuilder implements Builder<Carrier> {

	private String name, phone;

	private Long carrierNumber;

	private Address address;

	@Override
	public Carrier build() {
		Carrier c = new Carrier();
		c.setName(name);
		c.setPhone(phone);
		c.setCarrierNumber(carrierNumber);
		c.setAddress(address);
		return c;
	}

	public CarrierBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public CarrierBuilder setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public CarrierBuilder setCarrierNumber(Long carrierNumber) {
		this.carrierNumber = carrierNumber;
		return this;
	}

	public CarrierBuilder setAddress(Address address) {
		this.address = address;
		return this;
	}

	public CarrierBuilder ups() {
		setCarrierNumber(87L)
		.setAddress(new Address(
				"United Parcel Service Detschland Inc. & Co. OHG",
				null,
				"Görlitzer Straße 1",
				41460,
				"Neuss",
				Country.DEUTSCHLAND))
		.setPhone("01806 882 663")
		.setName("United Pacel Service");
		return this;
	}
	
	public CarrierBuilder dhl() {
		setCarrierNumber(38L)
		.setAddress(new Address(
				"Deutsche Post AG",
				null,
				"Charles-de-Gaulle-Str. 20",
				53113,
				"Bonn",
				Country.DEUTSCHLAND))
		.setPhone("+49 (0)228 182-0")
		.setName("Deutsche Post AG");
		return this;
	}

}
