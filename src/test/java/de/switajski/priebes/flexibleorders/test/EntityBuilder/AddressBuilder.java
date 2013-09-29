package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.parameter.Address;
import de.switajski.priebes.flexibleorders.reference.Country;

public class AddressBuilder implements Builder<Address>{

	private String name1 = "name1 ";

	private String name2 = "name2 ";

    private String street = "street";

    private int postalCode = 1;

    private String city = "city";

    private Country country = Country.GERMANY;
	
    public AddressBuilder withCountry(Country country){
    	this.country = country;
		return this;
    }
    
    public AddressBuilder withStandardAttributes(Integer i){
    	name1 = name1.concat(i.toString());
    	name2 = name2.concat(i.toString());
    	street = street.concat(i.toString());
    	postalCode = i;
    	city = city.concat(i.toString());
    	return this;
    }
    
	@Override
	public Address build() {
		Address address = new Address(
				name1, name2, street, postalCode, city, country);
		return address;
	}

}
