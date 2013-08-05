package de.switajski.priebes.flexibleorders.domain;
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
public class InvoiceItem extends Item {

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
    
    /**
     * The only way to create a InvoiceItem is to generate it from a ShippingItem.
     * This is done by {@link InvoiceItem#deliver} 
     * @param quantity 
     * @param shippingItem
     * @param transmitToSupplier
     */
    public InvoiceItem(ShippingItem shippingItem, int quantity, long invoiceNumber) {
    	setInvoiceNumber(invoiceNumber);
    	
    	historize(shippingItem);
    	setCreated(new Date());
    	setStatus(Status.SHIPPED);
    	setQuantity(quantity);
    	
    	Customer customer = shippingItem.getCustomer();
    	setInvoiceCity(customer.getCity());
    	setInvoiceCountry(customer.getCountry());
		setInvoiceName1(customer.getName1());
		setInvoiceName2(customer.getName2());
		setInvoicePostalCode(customer.getPostalCode());
		setInvoiceStreet(customer.getStreet());
    	
	}
    
    public InvoiceItem(){
    }
    
    public ArchiveItem complete(int quantity, long accountNumber){
    	setAccountNumber(accountNumber);
    	ArchiveItem ai = new ArchiveItem(this, quantity, accountNumber);
    	setStatus(Status.COMPLETED);
    	return ai;
    }

	@Override
	public int compareTo(Item o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
