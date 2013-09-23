package de.switajski.priebes.flexibleorders.domain;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.Status;

import javax.persistence.Enumerated;
import javax.validation.constraints.Min;

@RooJavaBean
@RooToString
@RooJpaEntity
@JsonIgnoreProperties(ignoreUnknown = true)
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

    /**
     * The only way to create a InvoiceItem is to generate it from a ShippingItem.
     * It is protected, because only {@link ShippingItem#deliver} should have access to it.
     * @param quantity
     * @param shippingItem the entity to deliver (used to historize)
     * @param transmitToSupplier
     */
    public InvoiceItem(ShippingItem shippingItem, int quantity, long invoiceNumber) {
    	if (shippingItem.getInvoiceNumber()==null) 
        	throw new IllegalArgumentException("Set the invoiceNumber of the Shipping Item before delivering! Use ShippingItem.deliver()");
        setInvoiceNumber(invoiceNumber);
        historize(shippingItem);
        setCreated(new Date());
        setQuantity(quantity);
        setQuantityLeft(quantity);
        Customer customer = shippingItem.getCustomer();
        setInvoiceCity(customer.getCity());
        setInvoiceCountry(customer.getCountry());
        setInvoiceName1(customer.getName1());
        setInvoiceName2(customer.getName2());
        setInvoicePostalCode(customer.getPostalCode());
        setInvoiceStreet(customer.getStreet());
    }

    public InvoiceItem() {
    }

    public ArchiveItem complete(int quantity, long accountNumber) {
        setAccountNumber(accountNumber);
        ArchiveItem ai = new ArchiveItem(this, quantity, accountNumber);
        addShippedQuantity(quantity);
        return ai;
    }

    public void addShippedQuantity(int quantity) {
    	this.setQuantityLeft(getQuantityLeft()-quantity);
	}
    
    public void reduceShippedQuantity(int quantity) {
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
    
    /**
     * sets a shipping item to a non shipped state.</br>
     * Has two use cases:</br>
     * 1. One shipping item is completely shipped by one invoice item.
     * 2. One shipping item is shipped by several invoice items. 
     * @param shippingItem
     */
    public void withdraw(ShippingItem shippingItem){
    	shippingItem.reduceShippedQuantity(this.getQuantity());
    	
    	// delete invoiceNumber only if one shipping item is completely shipped by one invoice item.
    	if (shippingItem.getQuantity() == shippingItem.getQuantityLeft())
    		shippingItem.setInvoiceNumber(null);
    }

    
}
