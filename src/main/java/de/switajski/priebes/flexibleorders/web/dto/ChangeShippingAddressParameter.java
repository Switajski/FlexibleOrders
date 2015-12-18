package de.switajski.priebes.flexibleorders.web.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.json.CountryDeserializer;
import de.switajski.priebes.flexibleorders.json.CountrySerializer;
import de.switajski.priebes.flexibleorders.json.EmptyStringStripToNullDeserializer;
import de.switajski.priebes.flexibleorders.reference.Country;

@JsonAutoDetect
public class ChangeShippingAddressParameter {

    @NotNull
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String name1,
            street,
            city;
    @JsonDeserialize(using = CountryDeserializer.class)
    @JsonSerialize(using = CountrySerializer.class)
    private Country country;
    @NotNull
    private Integer postalCode;
    private String name2;

    @NotNull
    private List<String> documentNumbers;

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public List<String> getDocumentNumbers() {
        return documentNumbers;
    }

    public void setDocumentNumbers(List<String> documentNumbers) {
        this.documentNumbers = documentNumbers;
    }

    public Address getAddress() {
        Address a = new Address();
        a.setCity(city);
        a.setCountry(country);
        a.setName1(name1);
        a.setName2(name2);
        a.setPostalCode(postalCode);
        a.setStreet(street);
        return a;
    }
}
