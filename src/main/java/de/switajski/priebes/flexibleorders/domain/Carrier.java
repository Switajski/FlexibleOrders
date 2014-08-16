package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Entity;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;

@Entity
public class Carrier extends GenericEntity{
	
	private String name, phone;
	
	private Long carrierNumber;
	
	private Address address;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Long getCarrierNumber() {
		return carrierNumber;
	}

	public void setCarrierNumber(Long carrierNumber) {
		this.carrierNumber = carrierNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
