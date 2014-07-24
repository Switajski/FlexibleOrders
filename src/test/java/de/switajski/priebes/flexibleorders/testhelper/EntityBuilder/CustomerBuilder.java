package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.reference.Country;

public class CustomerBuilder implements Builder<Customer> {

	private Address address;
	private String email;
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
		phone = "0049".concat(i.toString());
		return this;
	}

	public static Customer buildYvonne() {
		Customer yvonne = new CustomerBuilder(
				11L,
				"massa.Suspendisse@consequat.edu",
				new Address(
						"Yvonne",
						"Donaldson",
						"7800 Eu, Av.",
						954,
						"San Isidro",
						Country.DEUTSCHLAND))
				.build();
		return yvonne;
	}
	
	public Customer buildCustomerWyoming() {
		Customer wyoming = new CustomerBuilder(
				33L,
				"ullamcorper.Duis.cursus@dolorNullasemper.edu",
				new Address(
						"Wyoming",
						"Finley",
						"6340 Sed Avenue",
						1721,
						"Sens",
						Country.DEUTSCHLAND))
				.build();
		return wyoming;
	}

	public static Customer createCustomerJerome() {
		Customer jerome = new CustomerBuilder(
				55L,
				"lacus@luctusut.net",
				new Address(
						"Jerome",
						"Greer",
						"Ap #678-5842 Mauris Street",
						439,
						"De Haan",
						Country.DEUTSCHLAND))
				.build();
		return jerome;
	}

	public static Customer createCustomerEdward() {
		Customer edward = new CustomerBuilder(
				44L,
				"Phasellus.in.felis@purusaccumsaninterdum.co.uk",
				new Address(
						"Edward",
						"Turner",
						"P.O. Box 194, 6686 Non, St.",
						97066,
						"Savona",
						Country.UNITED_KINGDOM))
				.build();
		return edward;
	}

	public static Customer createCustomerNaida() {
		Customer naida = new CustomerBuilder(
				22L,
				"vel@laciniaSed.org",
				new Address(
						"Naida",
						"Horne",
						"3000 Vitae Road",
						2292,
						"Zelzate",
						Country.IRELAND))
				.build();
		return naida;
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
		c.setPhone(phone);
		c.setInvoiceAddress(address);

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

	public CustomerBuilder setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public CustomerBuilder setCustomerNumber(Long customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

}
