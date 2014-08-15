package de.switajski.priebes.flexibleorders.web.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.deser.std.StdDeserializer.BigDecimalDeserializer;

import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;

/**
 * Data Transfer Object for ExtJs GUI </br>
 * Build on <code>BestellpositionData</code>, which is written in JavaScript.
 * @author Marek Switajski
 *
 */
@JsonAutoDetect
public class ItemDto {

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
	@JsonDeserialize(using = BigDecimalDeserializer.class)
	private BigDecimal priceNet;
	private String status;
	private Date expectedDelivery;
	private String trackNumber;
	private String packageNumber;
	private String paymentConditions;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProduct() {
		return product;
	}
	public void setProduct(Long product) {
		this.product = product;
	}
	public Long getCustomer() {
		return customer;
	}
	public void setCustomer(Long customer) {
		this.customer = customer;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getQuantityLeft() {
		return quantityLeft;
	}
	public void setQuantityLeft(Integer quantityLeft) {
		this.quantityLeft = quantityLeft;
	}
	public BigDecimal getPriceNet() {
		return priceNet;
	}
	public void setPriceNet(BigDecimal priceNet) {
		this.priceNet = priceNet;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getExpectedDelivery() {
		return expectedDelivery;
	}
	public void setExpectedDelivery(Date expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;		
	}
	public String getDocumentNumber() {
		return documentNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getCreated() {
		return created;
	}
	@JsonDeserialize(using = JsonDateDeserializer.class)
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getOrderConfirmationNumber() {
		return orderConfirmationNumber;
	}
	public void setOrderConfirmationNumber(String orderConfirmationNumber) {
		this.orderConfirmationNumber = orderConfirmationNumber;
	}
	public String getTrackNumber() {
		return trackNumber;
	}
	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}
	public String getPackageNumber() {
		return packageNumber;
	}
	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}
	public Long getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(Long customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getDeliveryNotesNumber() {
		return deliveryNotesNumber;
	}
	public void setDeliveryNotesNumber(String deliveryNotesNumber) {
		this.deliveryNotesNumber = deliveryNotesNumber;
	}
	public void setPaymentConditions(String paymentConditions) {
		this.paymentConditions = paymentConditions;
		
	}
	public String getPaymentConditions() {
		return paymentConditions;
	}
	public String getReceiptNumber() {
		return receiptNumber;
	}
	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemDto other = (ItemDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName() + ": " + getQuantityLeft()+ " x " + getProductName();
	}
}
