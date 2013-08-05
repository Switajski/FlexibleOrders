package de.switajski.priebes.flexibleorders.domain;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.Status;

import javax.persistence.Enumerated;

@RooJavaBean
@RooToString
@RooJpaEntity
public class ShippingItem extends Item {

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
	 * @param quantity 
     * @param transmitToSupplier
     */
    public ShippingItem(OrderItem orderItem, int quantity, Boolean transmitToSupplier, long orderConfirmationNumber) {
    	if (transmitToSupplier) throw new UnsupportedOperationException("Implement me!");
    	
    	this.setOrderConfirmationNumber(orderConfirmationNumber);
    	historize(orderItem);
    	setTransmitToSupplier(transmitToSupplier);
		setCreated(new Date());
		setStatus(Status.CONFIRMED);
		setQuantity(quantity);
		Customer customer = orderItem.getCustomer();
		
		//TODO: Create @Embedded class shippingAddress
		setShippingCity(customer.getCity());
		setShippingCountry(customer.getCountry());
		setShippingName1(customer.getName1());
		setShippingName2(customer.getName2());
		setShippingPostalCode(customer.getPostalCode());
		setShippingStreet(customer.getStreet());
	}

	public InvoiceItem deliver(int quantity, long invoiceNumber) {
		this.setInvoiceNumber(invoiceNumber);
		InvoiceItem ii = new InvoiceItem(this, quantity, invoiceNumber);
		this.setStatus(Status.SHIPPED);
		return ii;
	}
	
	@Override
	public int compareTo(Item o) {
		return 0;
	}

}
