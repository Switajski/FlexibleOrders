package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Customer;

public class CustomerBuilder implements Builder<Customer> {

    private String shortName;
    private Address address;
    private String email;
    private String password;
    private String phone;

    
    /**
     * 
     * @param street
     * @param city
     * @param postalCode
     * @param email
     */
    public CustomerBuilder(
    		String shortName,
    		String email,
    		Address address
    		) {
    	this.shortName = shortName;
    	this.email = email;
    	this.address = address;
    }
    
    public CustomerBuilder generateAttributes(Integer i){
    	address = AddressBuilder.buildWithGeneratedAttributes(i);
    	shortName = "shortName".concat(i.toString());
    	email = "name@somewhere.com".concat(i.toString());
    	password = "password".concat(i.toString());
    	phone = "0049".concat(i.toString());
    	return this;
    }
    
    public static Customer buildWithGeneratedAttributes(Integer i){
    	return new CustomerBuilder(
    			"shortName".concat(i.toString()),
    			"name@somewhere.com".concat(i.toString()),
    			AddressBuilder.buildWithGeneratedAttributes(i)
    			)
    	.build();
    }
    
	@Override
	public Customer build() {
		Customer c = new Customer();
		c.setShortName(shortName);
		c.setEmail(email);
		c.setPassword(password);
		c.setPhone(phone);
		c.setAddress(address);
		
		return c;
	}

	public CustomerBuilder setShortName(String shortName) {
		this.shortName = shortName;
		return this;
	}

	public CustomerBuilder setAddress(Address address) {
		this.address = address;
		return this;
	}

	public CustomerBuilder setEmail(String email) {
		this.email = email;
		return this;
	}

	public CustomerBuilder setPassword(String password) {
		this.password = password;
		return this;
	}

	public CustomerBuilder setPhone(String phone) {
		this.phone = phone;
		return this;
	}
	

}
