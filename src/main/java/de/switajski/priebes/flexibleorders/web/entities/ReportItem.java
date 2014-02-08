package de.switajski.priebes.flexibleorders.web.entities;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;

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
	private String orderNumber;
	private String documentNumber;
	private Integer quantity;
	private Integer quantityLeft;
	private BigDecimal priceNet;
	private String status;
	private Date expectedDelivery;
	
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
	@JsonDeserialize(using = JsonDateDeserializer.class)
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
}
