package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Invoice extends Report {

	private String paymentConditions;
	
	@NotNull
	private Address invoiceAddress;
	
	public Invoice(String invoiceNumber, String paymentConditions, Address invoiceAddress) {
		super(invoiceNumber);
		this.paymentConditions = paymentConditions;
		this.invoiceAddress = invoiceAddress;
	}
	
	
	public String getPaymentConditions() {
		return paymentConditions;
	}

	public void setPaymentConditions(String paymentConditions) {
		this.paymentConditions = paymentConditions;
	}


	public Address getInvoiceAddress() {
		return invoiceAddress;
	}


	public void setInvoiceAddress(Address invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

}
