package de.switajski.priebes.flexibleorders.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.Status;

import javax.persistence.Enumerated;
import javax.validation.constraints.Min;

@RooJavaBean
@RooToString
@RooJpaEntity
public class ArchiveItem extends Item {

    /**
     * The only way to create a ArchiveItem is to generate it from a IvoiceItem.
     * This is done by {@link IvoiceItem#complete}
     * @param quantity
     *
     * @param orderItem
     * @param transmitToSupplier
     */
    public ArchiveItem(InvoiceItem invoiceItem, int quantity, long accountNumber) {
        setAccountNumber(accountNumber);
        historize(invoiceItem);
        setCreated(new Date());
        setStatus(Status.COMPLETED);
        setQuantity(quantity);
        Customer customer = invoiceItem.getCustomer();
        customer = invoiceItem.getCustomer();
        setInvoiceCity(customer.getCity());
        setInvoiceCountry(customer.getCountry());
        setInvoiceName1(customer.getName1());
        setInvoiceName2(customer.getName2());
        setInvoicePostalCode(customer.getPostalCode());
        setInvoiceStreet(customer.getStreet());
        setShippingCity(customer.getCity());
        setShippingCountry(customer.getCountry());
        setShippingName1(customer.getName1());
        setShippingName2(customer.getName2());
        setShippingPostalCode(customer.getPostalCode());
        setShippingStreet(customer.getStreet());
        setAnNaeherei(false);
    }

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
}
