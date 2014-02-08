package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.reference.Country;

@Embeddable
public class Address {

	private String name1;
	
	@NotNull
	private String name2;

	@NotNull
    private String street;

    @NotNull
    private int postalCode;

    @NotNull
    private String city;

    @NotNull
    @Enumerated
    private Country country = Country.GERMANY;

    public Address(){};

    /**
     * 
     * @param name1
     * @param name2
     * @param street
     * @param postalCode
     * @param city
     * @param country
     */
	public Address(
			String name1, 
			String name2, 
			String street, 
			int postalCode, 
			String city,
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
