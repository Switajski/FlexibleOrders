package de.switajski.priebes.flexibleorders.web.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.deser.std.StdDeserializer.BigDecimalDeserializer;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.reference.Country;

/**
 * Can have several orders
 * @author Marek Switajski
 *
 */
@JsonAutoDetect
public class JsonCreateReportRequest {
	
	private Long customerId;
	
	private String paymentConditions;
	
	private Date created;

	private String name1, name2, street, city, country;
	
	private Integer postalCode;
	
	private String dname1, dname2, dstreet, dcity, dcountry;
	
	private Integer dpostalCode;
	
	private List<ItemDto> items;

	private String invoiceNumber, deliveryNotesNumber, orderNumber;
	
	private String trackNumber, packageNumber;

	private String billing;
	
	@JsonDeserialize(using = BigDecimalDeserializer.class)
	private BigDecimal shipment;

	private String orderConfirmationNumber;

	private Date expectedDelivery;

	private Long deliveryMethodNo;

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

	public List<ItemDto> getItems() {
		return items;
	}

	public void setItems(List<ItemDto> items) {
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
	
	public Address createInvoiceAddress(){
		return new Address(getName1(), getName2(), getStreet(), getPostalCode(), getCity(),
				Country.DEUTSCHLAND);
	}
	
	public Address createDeliveryAddress(){
		return new Address(getDname1(), getDname2(), getDstreet(), getDpostalCode(), getDcity(),
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
		for (ItemDto item:getItems()){
			if (item.getQuantity() < 1)
				throw new IllegalArgumentException("Menge von "+item.getProductName()+" ist kleiner als 1");
			if (item.getPriceNet() == null)
				throw new IllegalArgumentException("Keinen Preis angegeben");
		}
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getCreated() {
		return created;
	}

	@JsonDeserialize(using = JsonDateDeserializer.class)
	public void setCreated(Date created) {
		this.created = created;
	}

	public String getDname1() {
		return dname1;
	}

	public void setDname1(String dname1) {
		this.dname1 = dname1;
	}

	public String getDname2() {
		return dname2;
	}

	public void setDname2(String dname2) {
		this.dname2 = dname2;
	}

	public String getDstreet() {
		return dstreet;
	}

	public void setDstreet(String dstreet) {
		this.dstreet = dstreet;
	}

	public String getDcity() {
		return dcity;
	}

	public void setDcity(String dcity) {
		this.dcity = dcity;
	}

	public String getDcountry() {
		return dcountry;
	}

	public void setDcountry(String dcountry) {
		this.dcountry = dcountry;
	}

	public Integer getDpostalCode() {
		return dpostalCode;
	}

	public void setDpostalCode(Integer dpostalCode) {
		this.dpostalCode = dpostalCode;
	}

	public String getBilling() {
		return billing;
	}

	public void setBilling(String billing) {
		this.billing = billing;
	}

	public Long getDeliveryMethodNo() {
		return deliveryMethodNo;
	}
	
	public void setDeliveryMethodNo(Long deliveryMethodNo) {
		this.deliveryMethodNo = deliveryMethodNo;
	}
}
