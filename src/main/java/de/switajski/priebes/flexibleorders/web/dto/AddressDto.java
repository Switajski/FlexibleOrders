package de.switajski.priebes.flexibleorders.web.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.reference.Country;

public class AddressDto {

    private String documentNumber;

    private String name1;
    private String name2;
    private String street;
    private Integer postalCode;
    private String city;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Country country;

    public AddressDto(Address address, String documentNumber) {
        this.documentNumber = documentNumber;
        name1 = address.getName1();
        name2 = address.getName2();
        street = address.getStreet();
        postalCode = address.getPostalCode();
        city = address.getCity();
        country = address.getCountry();
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
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

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}
