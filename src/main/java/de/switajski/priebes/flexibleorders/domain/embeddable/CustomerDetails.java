package de.switajski.priebes.flexibleorders.domain.embeddable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * @author Marek Switajski
 *
 */
@Embeddable
public class CustomerDetails {

	private String vendorNumber, vatIdNo, paymentConditions;
	private String saleRepresentative, mark;
	@Embedded
	private ContactInformation contactInformation;
	
	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	public String getVatIdNo() {
		return vatIdNo;
	}

	public void setVatIdNo(String valueAddedTaxIdNo) {
		this.vatIdNo = valueAddedTaxIdNo;
	}

	public void setPaymentConditions(String paymentConditions) {
		this.paymentConditions = paymentConditions;
	}

	public String getPaymentConditions() {
		return paymentConditions;
	}

	public String getMark() {
		return this.mark;
	}
	
	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getSaleRepresentative() {
		return this.saleRepresentative;
	}

	public void setSaleRepresentative(String saleRepresentative) {
		this.saleRepresentative = saleRepresentative;
	}

	public void setContactInformation(ContactInformation contactInformation) {
		this.contactInformation = contactInformation;
	}
	
	public ContactInformation getContactInformation(){
		return this.contactInformation;
	}
	
	@Override
	public boolean equals(Object obj) {
	    return EqualsBuilder.reflectionEquals(this, obj, false);
	}

	@Override
	public String toString(){
	    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public int hashCode(){
	    return HashCodeBuilder.reflectionHashCode(this, false);
	}
}
