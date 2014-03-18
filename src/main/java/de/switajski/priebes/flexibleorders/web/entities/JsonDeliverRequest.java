package de.switajski.priebes.flexibleorders.web.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.deser.std.StdDeserializer.BigDecimalDeserializer;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.reference.Country;

/**
 * Can have several orders
 * @author Marek Switajski
 *
 */
@JsonAutoDetect
public class JsonDeliverRequest {
	
	private Long customerId;
	
	private String paymentConditions;
	
	private String name1;
	
	private String name2;
	
	private String street;
	
	private Integer postalCode;
	
	private String city;
	
	private String country;
	
	private List<ReportItem> items;

	private String invoiceNumber;
	
	private String deliveryNotesNumber;
	
	private String trackNumber;
	
	private String packageNumber;

	private String orderNumber;
	
	@JsonDeserialize(using = BigDecimalDeserializer.class)
	private BigDecimal shipment;

	private String orderConfirmationNumber;

	private Date expectedDelivery;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
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

	public List<ReportItem> getItems() {
		return items;
	}

	public void setItems(List<ReportItem> items) {
		this.items = items;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOrderNumber() {
		return this.orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public Address createAddress(){
		return new Address(getName1(), getName2(), getStreet(), getPostalCode(), getCity(),
				Country.DEUTSCHLAND);
	}

	public String getOrderConfirmationNumber() {
		return this.orderConfirmationNumber;
	}

	public void setOrderConfirmationNumber(String orderConfirmationNumber) {
		this.orderConfirmationNumber = orderConfirmationNumber;
	}
	
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getExpectedDelivery() {
		return expectedDelivery;
	}

	@JsonDeserialize(using = JsonDateDeserializer.class)
	public void setExpectedDelivery(Date expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
	}

	public BigDecimal getShipment() {
		return shipment;
	}

	public void setShipment(BigDecimal shipment) {
		this.shipment = shipment;
	}

	public String getPaymentConditions() {
		return paymentConditions;
	}

	public void setPaymentConditions(String paymentConditions) {
		this.paymentConditions = paymentConditions;
	}

	public String getDeliveryNotesNumber() {
		return deliveryNotesNumber;
	}

	public void setDeliveryNotesNumber(String deliveryNotesNumber) {
		this.deliveryNotesNumber = deliveryNotesNumber;
	}
	
	public void validate(){
		if (getItems().isEmpty())
			throw new IllegalArgumentException("Keine Positionen angegeben!");
		for (ReportItem item:getItems())
			if (item.getQuantity() < 1)
				throw new IllegalArgumentException("Menge von "+item.getProductName()+" ist kleiner als 1");
	}
}
