package de.switajski.priebes.flexibleorders.domain.parameter;

import de.switajski.priebes.flexibleorders.reference.Country;

public class Address {
	
	private String name2;

    private String street;

    private String city;

    private int postalCode;

    private Country country;

	private String name1;

	public Address(String name1, 
			String name2, 
			String street, 
			int postalCode, 
			String city, 
			Country country) {
	}

    public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	
}