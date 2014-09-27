package de.switajski.priebes.flexibleorders.domain.embeddable;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import de.switajski.priebes.flexibleorders.reference.Country;

@Embeddable
public class Address {

    private String name1;

    @NotNull
    private String name2;

    @NotNull
    private String street;

    @NotNull
    private Integer postalCode;

    @NotNull
    private String city;

    @NotNull
    @Enumerated
    private Country country = Country.DEUTSCHLAND;

    public Address() {};

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
            Integer postalCode,
            String city,
            Country country) {
        setName1(name1);
        setName2(name2);
        setStreet(street);
        setPostalCode(postalCode);
        setCity(city);
        if (country != null) setCountry(country);
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

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public boolean isComplete() {
        if (name1 == null && name2 == null) 
            return false;
        if (street == null || city == null || postalCode < 1) 
            return false;
        return true;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

}
