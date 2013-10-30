package de.switajski.priebes.flexibleorders.domain.parameter;

import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.reference.Country;

public class Address {

	private String name1;
	
	private String name2;

    private String street;

    private int postalCode;

    private String city;

    private Country country = Country.GERMANY;


	public Address(
			@NotNull String name1, 
			@NotNull String name2, 
			@NotNull String street, 
			@NotNull int postalCode, 
			@NotNull String city,
			Country country) {
		setName1(name1);
		setName2(name2);
		setStreet(street);
		setPostalCode(postalCode);
		setCity(city);
		if (country!=null) setCountry(country); 
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
