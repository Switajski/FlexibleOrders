package de.switajski.priebes.flexibleorders.domain;
import java.util.Date;

import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.Status;

@RooJavaBean
@RooToString
@RooJpaEntity
public class ShippingItem extends Item {

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date expectedDelivery;

    public Boolean getTransmitToSupplier() {
        return transmitToSupplier;
    }

    public void setTransmitToSupplier(Boolean transmitToSupplier) {
        this.transmitToSupplier = transmitToSupplier;
    }

    public String getShippingName1() {
        return shippingName1;
    }

    public void setShippingName1(String shippingName1) {
        this.shippingName1 = shippingName1;
    }

    public String getShippingName2() {
        return shippingName2;
    }

    public void setShippingName2(String shippingName2) {
        this.shippingName2 = shippingName2;
    }

    public String getShippingStreet() {
        return shippingStreet;
    }

    public void setShippingStreet(String shippingStreet) {
        this.shippingStreet = shippingStreet;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public int getShippingPostalCode() {
        return shippingPostalCode;
    }

    public void setShippingPostalCode(int shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode;
    }

    public Country getShippingCountry() {
        return shippingCountry;
    }

    public void setShippingCountry(Country shippingCountry) {
        this.shippingCountry = shippingCountry;
    }
    
    /**
     */
    private Integer quantityLeft;


    /**
     */
    @NotNull
    private Boolean transmitToSupplier;

    /**
     */
    private String shippingName1;

    /**
     */
    private String shippingName2;

    /**
     */
    @NotNull
    private String shippingStreet;

    /**
     */
    @NotNull
    private String shippingCity;

    /**
     */
    @NotNull
    private int shippingPostalCode;

    /**
     */
    @NotNull
    @Enumerated
    private Country shippingCountry;

    @Override
    public int compareTo(Item o) {
        return 0;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getExpectedDelivery() {
        return this.expectedDelivery;
    }

    @JsonDeserialize(using = JsonDateDeserializer.class)
    public void setExpectedDelivery(Date expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    
	public void deconfirm(OrderItem oi) {
		oi.reduceConfirmedQuantity(getQuantity());
		oi.setOrderConfirmationNumber(null);
	}
	
	/**
     * adds quantity. Afterwards the quantity left to reach the next SHIPPED State 
     * is more
     * @param quantity amount to add in quantityLeft
     */
	public void addShippedQuantity(int quantity) {
		setQuantityLeft(getQuantityLeft()-quantity);
	}
	
	/**
	 * reduces quantity. Afterwards the quantity left to reach the next SHIPPED State 
	 * is less
	 * @param quantity
	 */
	public void reduceShippedQuantity(int quantity) {
		setQuantityLeft(getQuantityLeft()+quantity);
	}
	
	//TODO: move to item
	@Override
	public Status getStatus(){
		if (getQuantityLeft()==0)
			return Status.SHIPPED;
		else return Status.CONFIRMED;
	}
}
