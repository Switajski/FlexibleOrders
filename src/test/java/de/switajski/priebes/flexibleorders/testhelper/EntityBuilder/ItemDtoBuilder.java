package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.math.BigDecimal;
import java.util.Date;

import de.switajski.priebes.flexibleorders.web.entities.ItemDto;

public class ItemDtoBuilder {

	private Long id;
	private Date created;
	private Long product;
	private String productName;
	private Long customer;
	private String customerName;
	private Long customerNumber;
	private String orderNumber;
	private String documentNumber;
	private String invoiceNumber;
	private String receiptNumber;
	private String deliveryNotesNumber;
	private String orderConfirmationNumber;
	private Integer quantity;
	private Integer quantityLeft;
	private BigDecimal priceNet;
	private String status;
	private Date expectedDelivery;
	private String trackNumber;
	private String packageNumber;
	private String paymentConditions;
	
	public ItemDto build(){
		ItemDto ri = new ItemDto();
		ri.setId(id);
		ri.setCreated(created);
		ri.setProduct(product);
		ri.setProductName(productName);
		ri.setCustomer(customer);
		ri.setCustomerName(customerName);
		ri.setCustomerNumber(customerNumber);
		ri.setOrderNumber(orderNumber);
		ri.setDocumentNumber(documentNumber);
		ri.setInvoiceNumber(invoiceNumber);
		ri.setReceiptNumber(receiptNumber);
		ri.setDeliveryNotesNumber(deliveryNotesNumber);
		ri.setOrderConfirmationNumber(orderConfirmationNumber);
		ri.setQuantity(quantity);
		ri.setQuantityLeft(quantityLeft);
		ri.setPriceNet(priceNet);
		ri.setStatus(status);
		ri.setExpectedDelivery(expectedDelivery);
		ri.setTrackNumber(trackNumber);
		ri.setPackageNumber(packageNumber);
		ri.setPackageNumber(packageNumber);
		ri.setPaymentConditions(paymentConditions);
		return ri;
	}
	
	public ItemDtoBuilder setId(Long id) {
		this.id = id;
		return this;
	}
	public ItemDtoBuilder setCreated(Date created) {
		this.created = created;
		return this;
	}
	public ItemDtoBuilder setProduct(Long product) {
		this.product = product;
		return this;
	}
	public ItemDtoBuilder setProductName(String productName) {
		this.productName = productName;
		return this;
	}
	public ItemDtoBuilder setCustomer(Long customer) {
		this.customer = customer;
		return this;
	}
	public ItemDtoBuilder setCustomerName(String customerName) {
		this.customerName = customerName;
		return this;
	}
	public ItemDtoBuilder setCustomerNumber(Long customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}
	public ItemDtoBuilder setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
		return this;
	}
	public ItemDtoBuilder setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
		return this;
	}
	public ItemDtoBuilder setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
		return this;
	}
	public ItemDtoBuilder setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
		return this;
	}
	public ItemDtoBuilder setDeliveryNotesNumber(String deliveryNotesNumber) {
		this.deliveryNotesNumber = deliveryNotesNumber;
		return this;
	}
	public ItemDtoBuilder setOrderConfirmationNumber(String orderConfirmationNumber) {
		this.orderConfirmationNumber = orderConfirmationNumber;
		return this;
	}
	public ItemDtoBuilder setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}
	public ItemDtoBuilder setQuantityLeft(Integer quantityLeft) {
		this.quantityLeft = quantityLeft;
		return this;
	}
	public ItemDtoBuilder setPriceNet(BigDecimal priceNet) {
		this.priceNet = priceNet;
		return this;
	}
	public ItemDtoBuilder setStatus(String status) {
		this.status = status;
		return this;
	}
	public ItemDtoBuilder setExpectedDelivery(Date expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
		return this;
	}
	public ItemDtoBuilder setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
		return this;
	}
	public ItemDtoBuilder setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
		return this;
	}
	public ItemDtoBuilder setPaymentConditions(String paymentConditions) {
		this.paymentConditions = paymentConditions;
		return this;
	}
	
}
