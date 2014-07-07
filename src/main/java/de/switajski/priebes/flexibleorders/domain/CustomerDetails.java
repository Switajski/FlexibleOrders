package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Embeddable;

@Embeddable
public class CustomerDetails {

	private String vendorNumber;
	
	private String vatIdNo;

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
}
