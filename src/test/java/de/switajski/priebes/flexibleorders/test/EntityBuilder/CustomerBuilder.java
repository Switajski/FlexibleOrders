package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.util.Date;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.reference.Country;

public class CustomerBuilder implements Builder<Customer> {

	private Long id;
    private String shortName;
    private String name1;
    private String name2;
    private String street;
    private String city;
    private int postalCode;
    private Country country = Country.GERMANY;
    private Date created = new Date();
    private String email;
    private String password;
    private String phone;
	
    public CustomerBuilder(
    		String street,
    		String city,
    		int postalCode,
    		String email) {
    	this.street = street;
    	this.city = city;
    	this.postalCode = postalCode;
    	this.email = email;
    }
    
    public CustomerBuilder generateAttributes(Integer i){
    	id = i.longValue();
    	shortName = "shortName".concat(i.toString());
    	name1 = "name1 ".concat(i.toString());
    	name2 = "name2 ".concat(i.toString());
    	street = "street ".concat(i.toString());
    	city = "city".concat(i.toString());
    	postalCode = i;
    	email = "name@somewhere.com".concat(i.toString());
    	password = "password".concat(i.toString());
    	phone = "0049".concat(i.toString());
    	return this;
    }
    
    public static Customer buildWithGeneratedAttributes(Integer i){
    	return new CustomerBuilder(
    			"street ".concat(i.toString()),
    			"city".concat(i.toString()),
    			i,
    			"name@somewhere.com".concat(i.toString()))
    	.build();
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
