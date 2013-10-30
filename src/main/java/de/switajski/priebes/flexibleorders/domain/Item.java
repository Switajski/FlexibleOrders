package de.switajski.priebes.flexibleorders.domain;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import de.switajski.priebes.flexibleorders.json.CustomerIdDeserializer;
import de.switajski.priebes.flexibleorders.json.CustomerToIdSerializer;
import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.json.ProductNumberDeserializer;
import de.switajski.priebes.flexibleorders.json.ProductToProductNumberSerializer;
import de.switajski.priebes.flexibleorders.reference.Status;

@Entity
@JsonAutoDetect
public abstract class Item extends GenericEntity implements Comparable<Item> {

	/**
	 */
	@NotNull
	@OneToOne
	protected Product product;

	/**
	 */
	@NotNull
	@ManyToOne
	protected Customer customer;

	/**
	 */
	@NotNull
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	protected Date created = new Date();

	/**
	 */
	@NotNull
	protected int quantity;

	/**
	 */
	@Min(0L)
	@NotNull
	protected BigDecimal priceNet;

	/**
	 */
	@Transient
	protected Status status;

	/**
	 */
	@NotNull
	protected String productName;

	/**
	 */
	@NotNull
	protected Long productNumber;

	/**
	 */
	protected Long orderConfirmationNumber;

	/**
	 */
	protected Long invoiceNumber;

	/**
	 */
	protected Long accountNumber;

	/**
	 */
	@NotNull
	protected Long orderNumber;


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
			this.setPriceNet(product.getPriceNet());
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

	public Status getStatus() {
		return this.status;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public void setOrderConfirmationNumber(Long orderConfirmationNumber) {
		this.orderConfirmationNumber = orderConfirmationNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getQuantity() {
        return this.quantity;
    }

	public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

	public BigDecimal getPriceNet() {
        return this.priceNet;
    }

	public void setPriceNet(BigDecimal priceNet) {
        this.priceNet = priceNet;
    }

	public String getProductName() {
        return this.productName;
    }

	public void setProductName(String productName) {
        this.productName = productName;
    }

	public Long getProductNumber() {
        return this.productNumber;
    }

	public void setProductNumber(Long productNumber) {
        this.productNumber = productNumber;
    }

	public Long getOrderConfirmationNumber() {
        return this.orderConfirmationNumber;
    }

	public Long getInvoiceNumber() {
        return this.invoiceNumber;
    }

	public Long getAccountNumber() {
        return this.accountNumber;
    }

	public Long getOrderNumber() {
        return this.orderNumber;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
