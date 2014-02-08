package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.reference.Country;

public class AddressBuilder implements Builder<Address>{

	private String name1;

	private String name2;

    private String street;

    private int postalCode;

    private String city;

    private Country country = Country.GERMANY;
	
    public AddressBuilder withCountry(Country country){
    	this.country = country;
		return this;
    }
    
    @Override
	public Address build() {
		Address address = new Address(
				name1, name2, street, postalCode, city, country);
		return address;
	}
	
	public AddressBuilder generateAttributes(Integer i){
    	name1 = "name1 ".concat(i.toString());
    	name2 = "name2 ".concat(i.toString());
    	street = "street ".concat(i.toString());
    	city = "city".concat(i.toString());
    	postalCode = i;
    	return this;
    }
	
	public static Address buildWithGeneratedAttributes(int i){
		return new AddressBuilder().generateAttributes(i).build();
	}

}
