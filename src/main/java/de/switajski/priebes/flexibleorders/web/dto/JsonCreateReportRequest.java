package de.switajski.priebes.flexibleorders.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.json.EmptyStringStripToNullDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.json.LocalDateDeserializer;
import de.switajski.priebes.flexibleorders.json.LocalDateSerializer;
import de.switajski.priebes.flexibleorders.reference.Country;

/**
 * TODO: try Jackson serializer with public attributes - anyhow this is a data
 * structure Can have several orders
 * 
 * @author Marek Switajski
 *
 */
@JsonAutoDetect
public class JsonCreateReportRequest {

    private Long customerId;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String paymentConditions,
            mark;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date created;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String name1,
            name2,
            street,
            city,
            country;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String
            contact1,
            contact2,
            contact3,
            contact4;

    private Integer postalCode;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String
            dname1,
            dname2,
            dstreet,
            dcity,
            dcountry;

    private Integer dpostalCode;

    @Valid
    @NotEmpty
    private List<ItemDto> items;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String invoiceNumber,
            deliveryNotesNumber,
            orderNumber;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String trackNumber,
            packageNumber;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String billing;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal shipment;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String orderConfirmationNumber;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate expectedDelivery;

    private Long deliveryMethodNo;

    private String saleRepresentative,
            valueAddedTaxIdNo,
            vendorNumber;

    private boolean showPricesInDeliveryNotes;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String discountText;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal discountRate;

    private boolean ignoreContradictoryExpectedDeliveryDates;

    private boolean singleDeliveryNotes;

    public Address createInvoiceAddress() {
        return new Address(name1, name2, street, postalCode, city,
                Country.DE);
    }

    public Address createDeliveryAddress() {
        return new Address(dname1, dname2, dstreet, dpostalCode, dcity,
                Country.DE);
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getPaymentConditions() {
        return paymentConditions;
    }

    public void setPaymentConditions(String paymentConditions) {
        this.paymentConditions = paymentConditions;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    public String getContact3() {
        return contact3;
    }

    public void setContact3(String contact3) {
        this.contact3 = contact3;
    }

    public String getContact4() {
        return contact4;
    }

    public void setContact4(String contact4) {
        this.contact4 = contact4;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getDeliveryNotesNumber() {
        return deliveryNotesNumber;
    }

    public void setDeliveryNotesNumber(String deliveryNotesNumber) {
        this.deliveryNotesNumber = deliveryNotesNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
    }

    public BigDecimal getShipment() {
        return shipment;
    }

    public void setShipment(BigDecimal shipment) {
        this.shipment = shipment;
    }

    public String getOrderConfirmationNumber() {
        return orderConfirmationNumber;
    }

    public void setOrderConfirmationNumber(String orderConfirmationNumber) {
        this.orderConfirmationNumber = orderConfirmationNumber;
    }

    public LocalDate getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(LocalDate expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public Long getDeliveryMethodNo() {
        return deliveryMethodNo;
    }

    public void setDeliveryMethodNo(Long deliveryMethodNo) {
        this.deliveryMethodNo = deliveryMethodNo;
    }

    public String getSaleRepresentative() {
        return saleRepresentative;
    }

    public void setSaleRepresentative(String saleRepresentative) {
        this.saleRepresentative = saleRepresentative;
    }

    public String getValueAddedTaxIdNo() {
        return valueAddedTaxIdNo;
    }

    public void setValueAddedTaxIdNo(String valueAddedTaxIdNo) {
        this.valueAddedTaxIdNo = valueAddedTaxIdNo;
    }

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public boolean isShowPricesInDeliveryNotes() {
        return showPricesInDeliveryNotes;
    }

    public void setShowPricesInDeliveryNotes(boolean showPricesInDeliveryNotes) {
        this.showPricesInDeliveryNotes = showPricesInDeliveryNotes;
    }

    public String getDiscountText() {
        return discountText;
    }

    public void setDiscountText(String discountText) {
        this.discountText = discountText;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public boolean isIgnoreContradictoryExpectedDeliveryDates() {
        return ignoreContradictoryExpectedDeliveryDates;
    }

    public void setIgnoreContradictoryExpectedDeliveryDates(boolean ignoreContradictoryExpectedDeliveryDates) {
        this.ignoreContradictoryExpectedDeliveryDates = ignoreContradictoryExpectedDeliveryDates;
    }

    public boolean isSingleDeliveryNotes() {
        return singleDeliveryNotes;
    }

    public void setSingleDeliveryNotes(boolean singleDeliveryNotes) {
        this.singleDeliveryNotes = singleDeliveryNotes;
    }

}
