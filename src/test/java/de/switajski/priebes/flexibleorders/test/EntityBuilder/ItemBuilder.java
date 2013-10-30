package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.math.BigDecimal;
import java.util.Date;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.reference.Status;

public abstract class ItemBuilder<T extends Item> implements Builder<T> {

	protected Product product;
	protected Customer customer;
	protected Date created = new Date();
	protected int quantity;
	protected BigDecimal priceNet;
	protected Status status;
	protected String productName;
	protected Long productNumber;
	protected Long orderConfirmationNumber;
	protected Long invoiceNumber;
	protected Long accountNumber;
	protected Long orderNumber;

	
	/**
	 * to be called in build-method of the concrete item class
	 */
	protected void setSuperAttributes(T item){
		item.setProduct(product);
		item.setCustomer(customer);
		item.setCreated(created);
		item.setQuantity(quantity);
		item.setPriceNet(priceNet);
		item.setStatus(status);
		item.setProductName(productName);
		item.setProductNumber(productNumber);
		item.setOrderConfirmationNumber(orderConfirmationNumber);
		item.setInvoiceNumber(invoiceNumber);
		item.setAccountNumber(accountNumber);
		item.setOrderNumber(orderNumber);
	}
	
	/**
	 * Constructor with minimal attributes to persist
	 * @param customer
	 * @param product
	 * @param orderNumber
	 * @param productNumber
	 * @param productName
	 * @param priceNet
	 */
	public ItemBuilder(Customer customer, Product product, 
			Long orderNumber, Long productNumber, 
			String productName, BigDecimal priceNet) {
		this.customer = customer;
		this.product = product;
		this.orderNumber = orderNumber;
		this.productNumber = productNumber;
		this.productName = productName;
		this.priceNet = priceNet;
	}
	

	public ItemBuilder<T> setProduct(Product product) {
		this.product = product;
		return this;
	}

	public ItemBuilder<T> setCustomer(Customer customer) {
		this.customer = customer;
		return this;
	}

	public ItemBuilder<T> setCreated(Date created) {
		this.created = created;
		return this;
	}

	public ItemBuilder<T> setQuantity(int quantity) {
		this.quantity = quantity;
		return this;
	}

	public ItemBuilder<T> setPriceNet(BigDecimal priceNet) {
		this.priceNet = priceNet;
		return this;
	}

	public ItemBuilder<T> setStatus(Status status) {
		this.status = status;
		return this;
	}

	public ItemBuilder<T> setProductName(String productName) {
		this.productName = productName;
		return this;
	}

	public ItemBuilder<T> setProductNumber(Long productNumber) {
		this.productNumber = productNumber;
		return this;
	}

	public ItemBuilder<T> setOrderConfirmationNumber(Long orderConfirmationNumber) {
		this.orderConfirmationNumber = orderConfirmationNumber;
		return this;
	}

	public ItemBuilder<T> setInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
		return this;
	}

	public ItemBuilder<T> setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
		return this;
	}

	public ItemBuilder<T> setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
		return this;
	}

}
