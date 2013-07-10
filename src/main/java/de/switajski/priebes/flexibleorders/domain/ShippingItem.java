package de.switajski.priebes.flexibleorders.domain;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.reference.Country;

import javax.persistence.Enumerated;

@RooJavaBean
@RooToString
@RooJpaEntity
public class ShippingItem extends Item {

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
    
	/**
     * The only way to create a ShippingItem is to generate it from a OrderItem.
     * This is done by {@link OrderItem#confirm} 
     * 
     * @param orderItem
     * @param transmitToSupplier
     */
    public ShippingItem(OrderItem orderItem, Boolean transmitToSupplier) {
    	historize(orderItem);
		setCreated(new Date());
		Customer customer = orderItem.getCustomer();
		
		//TODO: Create @Embedded class shippingAddress
		setShippingCity(customer.getCity());
		setShippingCountry(customer.getCountry());
		setShippingName1(customer.getName1());
		setShippingName2(customer.getName2());
		setShippingPostalCode(customer.getPostalCode());
		setShippingStreet(customer.getStreet());
	}
}
