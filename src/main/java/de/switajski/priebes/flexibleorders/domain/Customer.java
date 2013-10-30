package de.switajski.priebes.flexibleorders.domain;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import de.switajski.priebes.flexibleorders.domain.parameter.Address;
import de.switajski.priebes.flexibleorders.reference.Country;

@Entity
@JsonAutoDetect
public class Customer extends GenericEntity{

    /**
     */
    @Column(unique = true)
    private String shortName;

    /**
     */
    @JsonIgnore
    private String name1;

    /**
     */
    @JsonIgnore
    private String name2;

    /**
     */
    @JsonIgnore
    @NotNull
    private String street;

    /**
     */
    @JsonIgnore
    @NotNull
    private String city;

    /**
     */
    @JsonIgnore
    @NotNull
    private int postalCode;

    /**
     */
    @JsonIgnore
    @NotNull
    @Enumerated
    private Country country = Country.GERMANY;

    /**
     */
    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date created = new Date();

    /**
     */
    @NotNull
    @Column(unique = true)
    private String email;

    /**
     */
    @JsonIgnore
    private String password;

    /**
     */
    private String phone;
    
    public Customer(){}
    
    public Customer(
    		@NotNull String shortName,
    		@NotNull String email,
    		@NotNull Address address){
    	setAddress(address);
    	setEmail(email);
    	setShortName(shortName);
    }
    
    public void setAddress(Address address) {
    	name1 = address.getName1();
		name2 = address.getName2();
		street = address.getStreet();
		postalCode = address.getPostalCode();
		city = address.getCity();
		country = address.getCountry();
	}

	public Address getShippingAddress(){
    	return new Address(
    			getName1(),
    			getName2(),
    			getStreet(),
    			getPostalCode(),
    			getCity(),
    			getCountry()
    			);
    }
    
    //TODO: change domain to second address
    public Address getInvoiceAddress(){
    	return new Address(
    			getName1(),
    			getName2(),
    			getStreet(),
    			getPostalCode(),
    			getCity(),
    			getCountry()
    			);
    }

	public String getShortName() {
        return this.shortName;
    }

	public void setShortName(String shortName) {
        this.shortName = shortName;
    }

	public String getName1() {
        return this.name1;
    }

	public void setName1(String name1) {
        this.name1 = name1;
    }

	public String getName2() {
        return this.name2;
    }

	public void setName2(String name2) {
        this.name2 = name2;
    }

	public String getStreet() {
        return this.street;
    }

	public void setStreet(String street) {
        this.street = street;
    }

	public String getCity() {
        return this.city;
    }

	public void setCity(String city) {
        this.city = city;
    }

	public int getPostalCode() {
        return this.postalCode;
    }

	public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

	public Country getCountry() {
        return this.country;
    }

	public void setCountry(Country country) {
        this.country = country;
    }

	public Date getCreated() {
        return this.created;
    }

	public void setCreated(Date created) {
        this.created = created;
    }

	public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
        this.email = email;
    }

	public String getPassword() {
        return this.password;
    }

	public void setPassword(String password) {
        this.password = password;
    }

	public String getPhone() {
        return this.phone;
    }

	public void setPhone(String phone) {
        this.phone = phone;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
