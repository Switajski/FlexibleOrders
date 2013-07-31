package de.switajski.priebes.flexibleorders.domain;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import de.switajski.priebes.flexibleorders.json.CustomerIdDeserializer;
import de.switajski.priebes.flexibleorders.json.CustomerToIdSerializer;
import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.json.ProductNumberDeserializer;
import de.switajski.priebes.flexibleorders.json.ProductToProductNumberSerializer;
import de.switajski.priebes.flexibleorders.reference.Status;

import javax.persistence.Enumerated;

@JsonAutoDetect
@RooJavaBean
@RooToString
@RooJpaEntity(inheritanceType = "TABLE_PER_CLASS")
public abstract class Item implements Comparable<Item> {

    /**
     */
    @NotNull
    @OneToOne
    private Product product;

    /**
     */
    @NotNull
    @ManyToOne
    private Customer customer;

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
    private int quantity;

    /**
     */
    @Min(0L)
    @NotNull
    private BigDecimal priceNet;

    /**
     */
    @NotNull
    @Enumerated
    private Status status;

    /**
     */
    @NotNull
    private String productName;

    /**
     */
    @NotNull
    private Long productNumber;

    /**
     */
    private Long orderConfirmationNumber;

    /**
     */
    private Long invoiceNumber;

    /**
     */
    private Long accountNumber;

    /**
     */
    @NotNull
    private Long orderNumber;
    
    
    /**
     * Data like productnumber can change over time. 
     * In order to get time-specific data it is nessecary to historize it.
     * 
     * @param item from which data will be copied
     */
    public void historize(Item item){
    	setAccountNumber(item.getAccountNumber());
    	setCustomer(item.getCustomer());
    	setInvoiceNumber(item.getInvoiceNumber());
    	setOrderNumber(item.getOrderNumber());
    	setAccountNumber(item.getAccountNumber());
    	setOrderConfirmationNumber(item.getOrderConfirmationNumber());
    	setPriceNet(item.getPriceNet());
    	setProduct(item.getProduct());
    	setProductName(item.getProductName());
    	setProductNumber(item.getProductNumber());
    	setQuantity(item.getQuantity());
    }

	public void setStatus(Status status) {
        this.status = status;
    }

	@JsonDeserialize(using=ProductNumberDeserializer.class)
	public void setProduct(Product product) {
        this.product = product;
        this.productNumber = product.getProductNumber();
        this.productName = product.getName();
        if (getPriceNet()==null){
        	this.priceNet = product.getPriceNet();
        }
    }
	
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getCreated() {
        return this.created;
    }

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setCreated(Date created) {
        this.created = created;
    }

	@JsonSerialize(using=CustomerToIdSerializer.class)
	public Customer getCustomer() {
        return this.customer;
    }

	@JsonDeserialize(using=CustomerIdDeserializer.class)
	public void setCustomer(Customer customer) {
        this.customer = customer;
    }

	@JsonSerialize(using=ProductToProductNumberSerializer.class)
	public Product getProduct() {
        return this.product;
    }
}
