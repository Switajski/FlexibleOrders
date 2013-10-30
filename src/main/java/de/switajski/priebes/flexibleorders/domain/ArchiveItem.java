package de.switajski.priebes.flexibleorders.domain;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.Status;

@Entity
public class ArchiveItem extends Item {

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date expectedDelivery;

    /**
     */
    @NotNull
    private Boolean anNaeherei;

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
     */
    private String invoiceName1;

    /**
     */
    private String invoiceName2;

    /**
     */
    @NotNull
    private String invoiceStreet;

    /**
     */
    @NotNull
    private String invoiceCity;

    /**
     */
    @NotNull
    private int invoicePostalCode;

    /**
     */
    @NotNull
    @Enumerated
    private Country invoiceCountry;

    @Override
    public int compareTo(Item o) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     */
    @NotNull
    @Min(0L)
    private int quantityLeft;
    
    @Override
	public Status getStatus(){
		return Status.COMPLETED;
	}
    
    public void addCompletedQuantity(int quantity){
    	this.setQuantityLeft(getQuantityLeft()-quantity);
    }
 
    public void reduceCompletedQuantity(int quantity) {
    	this.setQuantityLeft(getQuantityLeft()+quantity);
	}

	public Date getExpectedDelivery() {
		return expectedDelivery;
	}

	public void setExpectedDelivery(Date expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
	}

	public Boolean getAnNaeherei() {
		return anNaeherei;
	}

	public void setAnNaeherei(Boolean anNaeherei) {
		this.anNaeherei = anNaeherei;
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

	public String getInvoiceName1() {
		return invoiceName1;
	}

	public void setInvoiceName1(String invoiceName1) {
		this.invoiceName1 = invoiceName1;
	}

	public String getInvoiceName2() {
		return invoiceName2;
	}

	public void setInvoiceName2(String invoiceName2) {
		this.invoiceName2 = invoiceName2;
	}

	public String getInvoiceStreet() {
		return invoiceStreet;
	}

	public void setInvoiceStreet(String invoiceStreet) {
		this.invoiceStreet = invoiceStreet;
	}

	public String getInvoiceCity() {
		return invoiceCity;
	}

	public void setInvoiceCity(String invoiceCity) {
		this.invoiceCity = invoiceCity;
	}

	public int getInvoicePostalCode() {
		return invoicePostalCode;
	}

	public void setInvoicePostalCode(int invoicePostalCode) {
		this.invoicePostalCode = invoicePostalCode;
	}

	public Country getInvoiceCountry() {
		return invoiceCountry;
	}

	public void setInvoiceCountry(Country invoiceCountry) {
		this.invoiceCountry = invoiceCountry;
	}

	public int getQuantityLeft() {
		return quantityLeft;
	}

	public void setQuantityLeft(int quantityLeft) {
		this.quantityLeft = quantityLeft;
	}

}
