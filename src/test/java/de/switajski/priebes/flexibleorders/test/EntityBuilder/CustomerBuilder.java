package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.util.Date;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.reference.Country;

public class CustomerBuilder implements Builder<Customer> {

	private Long id = 1l;
    private String shortName = "shortName";
    private String name1 = "name1 ";
    private String name2 = "name2 ";
    private String street = "street ";
    private String city = "city";
    private int postalCode = 1;
    private Country country = Country.GERMANY;
    private Date created = new Date();
    private String email = "name@somewhere.com";
    private String password = "password";
    private String phone = "0049";
	
    public CustomerBuilder() {}
    
    public CustomerBuilder withAttributes(Integer i){
    	id = i.longValue();
    	shortName = shortName.concat(i.toString());
    	name1 = name1.concat(i.toString());
    	name2 = name2.concat(i.toString());
    	street = street.concat(i.toString());
    	city = city.concat(i.toString());
    	postalCode = i;
    	email = email.concat(i.toString());
    	password = password.concat(i.toString());
    	phone = phone.concat(i.toString());
    	return this;
    }
    
	@Override
	public Customer build() {
		Customer c = new Customer();
		c.setId(id);
		c.setShortName(shortName);
		c.setName1(name1);
		c.setName2(name2);
		c.setStreet(street);
		c.setCity(city);
		c.setPostalCode(postalCode);
		c.setCountry(country);
		c.setCreated(created);
		c.setEmail(email);
		c.setPassword(password);
		c.setPhone(phone);
		
		return c;
	}

}
