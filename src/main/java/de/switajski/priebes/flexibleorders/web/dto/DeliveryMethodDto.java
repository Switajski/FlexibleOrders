package de.switajski.priebes.flexibleorders.web.dto;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * TODO this object is disposable by good serialization
 * @author Marek Switajski
 *
 */
@JsonAutoDetect
public class DeliveryMethodDto {

	private Long id, deliveryMethodNo;
	
    private String name, name1, name2, street, city, country;
    
    private int postalCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public void setName(String name) {
		this.name1 = name;
	}

	public String getName() {
		return name;
	}

    public void setDeliveryMethodNo(Long deliveryMethodNo) {
        this.deliveryMethodNo = deliveryMethodNo;
    }

    public Long getDeliveryMethodNo() {
        return deliveryMethodNo;
    }
    
}