package de.switajski.priebes.flexibleorders.domain;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.Status;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceItem extends Item {

    /**
     */
    private String invoiceName1;

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

	public String getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}

	public String getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

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
    
    /**
     */
    private String packageNumber;

    /**
     */
    private String trackNumber;

    /**
     */
    @NotNull
    @Min(0L)
    private int quantityLeft;

    public InvoiceItem() {
    }

    public void addCompletedQuantity(int quantity) {
    	this.setQuantityLeft(getQuantityLeft()-quantity);
	}
    
    public void reduceCompletedQuantity(int quantity) {
    	this.setQuantityLeft(getQuantityLeft()+quantity);
	}

	@Override
    public int compareTo(Item o) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
	public Status getStatus(){
		if (getQuantityLeft()==0)
			return Status.COMPLETED;
		else return Status.SHIPPED;
	}

	public int getQuantityLeft() {
        return this.quantityLeft;
    }

	public void setQuantityLeft(int quantityLeft) {
        this.quantityLeft = quantityLeft;
    }
}
