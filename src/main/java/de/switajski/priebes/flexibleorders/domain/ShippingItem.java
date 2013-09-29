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

import de.switajski.priebes.flexibleorders.domain.parameter.Address;
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

	public Boolean getTransmitToSupplier() {
		return transmitToSupplier;
	}
	
	public void setTransmitToSupplier(Boolean transmitToSupplier) {
		this.transmitToSupplier = transmitToSupplier;
	}
	
	public void setShippingAddress(Address address){
		shippingName1 = address.getName1();
		shippingName2 = address.getName2();
		shippingStreet = address.getStreet();
		shippingPostalCode = address.getPostalCode();
		shippingCity = address.getCity();
		shippingCountry = address.getCountry();
	}
	
	public Address getShippingAddress(){
		return new Address(shippingName1,
				shippingName2,
				shippingStreet,
				shippingPostalCode,
				shippingCity,
				shippingCountry);
	}
}

