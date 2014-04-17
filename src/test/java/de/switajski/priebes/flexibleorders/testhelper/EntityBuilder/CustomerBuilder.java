package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Customer;

public class CustomerBuilder implements Builder<Customer> {

	private Address address;
	private String email;
	private String password;
	private String phone;
	private Long customerNumber;

	public CustomerBuilder(
			Long customerNumber,
			String email,
			Address address) {
		this.email = email;
		this.address = address;
		this.customerNumber = customerNumber;
	}

	public CustomerBuilder generateAttributes(Integer i) {
		customerNumber = new Long(i.toString());
		address = AddressBuilder.buildWithGeneratedAttributes(i);
		email = "name@somewhere.com".concat(i.toString());
		password = "password".concat(i.toString());
		phone = "0049".concat(i.toString());
		return this;
	}

	public static Customer buildWithGeneratedAttributes(Integer i) {
		return new CustomerBuilder(Long.valueOf(i.toString()),
				"name@somewhere.com".concat(i.toString()),
				AddressBuilder.buildWithGeneratedAttributes(i))
				.build();
	}

	@Override
	public Customer build() {
		Customer c = new Customer();
		c.setCustomerNumber(customerNumber);
		c.setEmail(email);
		c.setPassword(password);
		c.setPhone(phone);
		c.setAddress(address);

		return c;
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

	public CustomerBuilder setCustomerNumber(Long customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

}
