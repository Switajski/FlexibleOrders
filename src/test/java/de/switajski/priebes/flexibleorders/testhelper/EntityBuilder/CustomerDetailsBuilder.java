package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;

public class CustomerDetailsBuilder implements Builder<CustomerDetails> {

	private String vendorNumber, vatIdNo, paymentConditions;
	private String saleRepresentative, mark;
	private ContactInformation contactInformation;
	
	@Override
	public CustomerDetails build() {
		CustomerDetails c = new CustomerDetails();
		c.setVendorNumber(vendorNumber);
		c.setVatIdNo(vatIdNo);
		c.setPaymentConditions(paymentConditions);
		c.setSaleRepresentative(saleRepresentative);
		c.setMark(mark);
		c.setContactInformation(contactInformation);
		return c;
	}

	public CustomerDetailsBuilder setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
		return this;
	}

	public CustomerDetailsBuilder setVatIdNo(String vatIdNo) {
		this.vatIdNo = vatIdNo;
		return this;
	}

	public CustomerDetailsBuilder setPaymentConditions(String paymentConditions) {
		this.paymentConditions = paymentConditions;
		return this;
	}

	public CustomerDetailsBuilder setSaleRepresentative(String saleRepresentative) {
		this.saleRepresentative = saleRepresentative;
		return this;
	}

	public CustomerDetailsBuilder setMark(String mark) {
		this.mark = mark;
		return this;
	}

	public CustomerDetailsBuilder setContactInformation(ContactInformation contactInformation) {
		this.contactInformation = contactInformation;
		return this;
	}

}
