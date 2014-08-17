package de.switajski.priebes.flexibleorders.domain.report;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.AgreementDetails;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;

/**
 * 
 * @author Marek Switajski
 *
 */
@Entity
public class OrderAgreement extends Report {

	@Embedded
	private CustomerDetails customerDetails;
	
	@Embedded
	private AgreementDetails agreementDetails;
	
	private String orderConfirmationNumber;

	public OrderAgreement() {
	}

	/**
	 * 
	 * @param orderAgreementNumber
	 * @param invoiceAddress
	 * @param shippingAddress
	 * @param confirmedSpec
	 */
	public OrderAgreement(
			String orderAgreementNumber,
			Address invoiceAddress,
			Address shippingAddress) {
		super(orderAgreementNumber);
		AgreementDetails ad = new AgreementDetails();
		ad.setInvoiceAddress(invoiceAddress);
		ad.setShippingAddress(shippingAddress);
		setAgreementDetails(ad);
	}

	public void setCustomerDetails(CustomerDetails customerDetails){
		this.customerDetails = customerDetails;
	}

	public CustomerDetails getCustomerDetails() {
		return this.customerDetails;
	}

	public AgreementDetails getAgreementDetails() {
		return agreementDetails;
	}

	public void setAgreementDetails(AgreementDetails agreementDetails) {
		this.agreementDetails = agreementDetails;
	}

	public String getOrderConfirmationNumber() {
		return orderConfirmationNumber;
	}

	public void setOrderConfirmationNumber(String orderConfirmationNumber) {
		this.orderConfirmationNumber = orderConfirmationNumber;
	}

}
