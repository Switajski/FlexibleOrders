package de.switajski.priebes.flexibleorders.web.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.reference.Country;

/**
 * TODO this object is disposable by good serialization
 * @author Marek Switajski
 *
 */
@JsonAutoDetect
public class JsonCustomer {

	private Long id;
	
	private Long customerNumber;
	
    private String email;

    private String password;

    private String phone;
    
    private String firstName;
    
    private String lastName;
    
    
    //*********************
    // Address attributes
    //*********************
    private String name1;

    private String name2;

    private String street;

    private int postalCode;

    private String city;

    private String country;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Customer toCustomer() {
		if (name1 == null) name1 = firstName;
		if (name2 == null) name2 = lastName;
		Address a = new Address(getName1(), getName2(), getStreet(), getPostalCode(), 
				getCity(), Country.GERMANY);
		Customer customer = new Customer(getCustomerNumber(), getEmail(), a);
		customer.setFirstName(getFirstName());
		customer.setLastName(getLastName());
		customer.setEmail(getEmail());
		customer.setPhone(getPhone());
		return customer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(Long customerNumber) {
		this.customerNumber = customerNumber;
	} 

}
