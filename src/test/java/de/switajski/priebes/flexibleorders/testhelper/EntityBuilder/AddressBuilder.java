package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.reference.Country;

public class AddressBuilder implements Builder<Address> {

	private String name1;

	private String name2;

	private String street;

	private int postalCode;

	private String city;

	private Country country = Country.DEUTSCHLAND;

	@Override
	public Address build() {
		Address address = new Address(
				name1, name2, street, postalCode, city, country);
		return address;
	}

	public AddressBuilder generateAttributes(Integer i) {
		name1 = "name1 ".concat(i.toString());
		name2 = "name2 ".concat(i.toString());
		street = "street ".concat(i.toString());
		city = "city".concat(i.toString());
		postalCode = i;
		return this;
	}

	public static Address buildWithGeneratedAttributes(int i) {
		return new AddressBuilder().generateAttributes(i).build();
	}

	public AddressBuilder setName1(String name1) {
		this.name1 = name1;
		return this;
	}

	public AddressBuilder setName2(String name2) {
		this.name2 = name2;
		return this;
	}

	public AddressBuilder setStreet(String street) {
		this.street = street;
		return this;
	}

	public AddressBuilder setPostalCode(int postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	public AddressBuilder setCity(String city) {
		this.city = city;
		return this;
	}

	public AddressBuilder setCountry(Country country) {
		this.country = country;
		return this;
	}

	public static Address createDefault() {
		return new AddressBuilder()
				.setCity("Hamburg")
				.setCountry(Country.DEUTSCHLAND)
				.setName1("Marek")
				.setName2("Switajski")
				.setPostalCode(20255)
				.setStreet("Stellinger Weg 14a")
				.build();
	}

}
