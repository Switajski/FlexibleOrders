package de.switajski.priebes.flexibleorders.domain;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.domain.parameter.Address;
import de.switajski.priebes.flexibleorders.reference.Country;

import javax.persistence.Enumerated;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@JsonAutoDetect
@RooJavaBean
@RooToString
@RooJpaEntity
public class Customer {

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
    private Country country;

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
}
