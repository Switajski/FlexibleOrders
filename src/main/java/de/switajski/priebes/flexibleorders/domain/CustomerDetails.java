package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Embeddable;

@Embeddable
public class CustomerDetails {

	private String vendorNumber, vatIdNo, paymentConditions;
	private String saleRepresentative, mark, contact1, contact2, contact3;
	
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

	public String getContact3() {
		return this.contact3;
	}

	public String getContact2() {
		return this.contact2;
	}

	public String getContact1() {
		return this.contact1;
	}

	public String getSaleRepresentative() {
		return this.saleRepresentative;
	}

	public void setSaleRepresentative(String saleRepresentative) {
		this.saleRepresentative = saleRepresentative;
	}

	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}

	public void setContact2(String contact2) {
		this.contact2 = contact2;
	}

	public void setContact3(String contact3) {
		this.contact3 = contact3;
	}
}
