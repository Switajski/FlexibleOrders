package de.switajski.priebes.flexibleorders.web.entities;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.deser.std.StdDeserializer.BigDecimalDeserializer;

import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;

/**
 * View class for GUI - Build on BestellpositionData
 * @author Marek Switajski
 *
 */
@JsonAutoDetect
public class ReportItem {

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
}
