package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.reference.Country;

public class CustomerBuilder implements Builder<Customer> {

	private Long customerNumber;
    private Address invoiceAddress;
    private Address shippingAddress;
    private String email;
    private CustomerDetails details;
    private String phone;
    private String firstName;
    private String lastName;
    private String companyName;
    private int paymentGracePeriod;

    @Override
	public Customer build() {
		Customer c = new Customer();
		c.setCustomerNumber(customerNumber);
		c.setInvoiceAddress(invoiceAddress);
		c.setShippingAddress(shippingAddress);
		c.setEmail(email);
		c.setDetails(details);
		c.setPhone(phone);
		c.setFirstName(firstName);
		c.setLastName(lastName);
		c.setPaymentGracePeriod(paymentGracePeriod);
		return c;
	}
    
	public CustomerBuilder(
			Long customerNumber,
			String email,
			Address address) {
		this.email = email;
		this.shippingAddress = address;
		this.invoiceAddress = address;
		this.customerNumber = customerNumber;
	}

	public CustomerBuilder() {
		
	}

	public CustomerBuilder generateAttributes(Integer i) {
		customerNumber = new Long(i.toString());
		invoiceAddress = AddressBuilder.buildWithGeneratedAttributes(i);
		email = "name@somewhere.com".concat(i.toString());
		phone = "0049".concat(i.toString());
		return this;
	}
	
	public CustomerBuilder yvonne() {
		this.setCustomerNumber(11L)
			.setEmail("massa.Suspendisse@consequat.edu")
			.setCompanyName("Baby Toys")
			.setFirstName("Yvonne")
			.setLastName("Donaldson")
			.setShippingAddress(
				new Address(
						"Yvonne",
						"Donaldson",
						"7800 Eu, Av.",
						954,
						"San Isidro",
						Country.DEUTSCHLAND))
			.setInvoiceAddress(
					new Address(
						"Donaldson Ent.",
						"Business Unit Sales",
						"7800 Eu, Av.",
						954,
						"San Isidro",
						Country.DEUTSCHLAND))
			.setDetails(
					new CustomerDetailsBuilder()
					.setContactInformation(
							new ContactInformationBuilder()
							.setContact1("Herr Priebe")
							.setContact2("Tel.Nr.: 0715897651")
							.setContact3("Fax Nr.: 0715897652")
							.setContact4("info@priebes.eu")
							.build())
					.setMark("Yvonnes Happy Baby Shop")
					.setPaymentConditions("Net 10 - Payment ten days after invoice date")
					.setVatIdNo("DE 12869876")
					.setVendorNumber("DE-Priebes")
					.build()
					);
		return this;
	}
	
	public CustomerBuilder wyoming() {
		Address address = new Address(
				"Wyoming",
				"Finley",
				"6340 Sed Avenue",
				1721,
				"Sens",
				Country.DEUTSCHLAND);
		this.setCustomerNumber(33L)
			.setFirstName("Wyoming")
			.setEmail("ullamcorper.Duis.cursus@dolorNullasemper.edu")
			.setShippingAddress(address)
			.setInvoiceAddress(address);
		return this;
	}

	public CustomerBuilder jerome() {
		Address address = new Address(
					"Jerome",
					"Greer",
					"Ap #678-5842 Mauris Street",
					439,
					"De Haan",
					Country.DEUTSCHLAND);
		this.setCustomerNumber(55L)
			.setLastName("Greer")
			.setEmail("lacus@luctusut.net")
			.setInvoiceAddress(address)
			.setShippingAddress(address);
		return this;
	}

	public CustomerBuilder edward() {
		Address address = new Address(
					"Edward",
					"Turner",
					"P.O. Box 194, 6686 Non, St.",
					97066,
					"Savona",
					Country.UNITED_KINGDOM);
		this.setCustomerNumber(44L)
			.setEmail("Phasellus.in.felis@purusaccumsaninterdum.co.uk")
			.setInvoiceAddress(address)
			.setShippingAddress(address);
		return this;
	}

	public CustomerBuilder naida() {
		Address address = new Address(
					"Naida",
					"Horne",
					"3000 Vitae Road",
					2292,
					"Zelzate",
					Country.IRELAND);
		this.setCustomerNumber(22L)
			.setEmail("vel@laciniaSed.org")
			.setShippingAddress(address)
			.setInvoiceAddress(address);
		return this;
	}

	public static Customer buildWithGeneratedAttributes(Integer i) {
		return new CustomerBuilder(Long.valueOf(i.toString()),
				"name@somewhere.com".concat(i.toString()),
				AddressBuilder.buildWithGeneratedAttributes(i))
				.build();
	}

	public CustomerBuilder setShippingAddress(Address address) {
		this.shippingAddress = address;
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

	public CustomerBuilder setInvoiceAddress(Address invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
		return this;
	}

	public CustomerBuilder setDetails(CustomerDetails details) {
		this.details = details;
		return this;
	}
	
	public CustomerBuilder setCompanyName(String companyName){
	    this.companyName = companyName;
	    return this;
	}

	public CustomerBuilder setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public CustomerBuilder setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public CustomerBuilder setPaymentGracePeriod(int paymentGracePeriod) {
		this.paymentGracePeriod = paymentGracePeriod;
		return this;
	}

}
