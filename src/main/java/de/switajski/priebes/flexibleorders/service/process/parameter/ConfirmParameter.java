package de.switajski.priebes.flexibleorders.service.process.parameter;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.json.EmptyStringStripToNullDeserializer;
import de.switajski.priebes.flexibleorders.json.LocalDateDeserializer;
import de.switajski.priebes.flexibleorders.json.LocalDateSerializer;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@JsonAutoDetect
public class ConfirmParameter {
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String orderNumber;
    @NotNull
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    @JsonProperty("orderConfirmationNumber")
    private String confirmNumber;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate expectedDelivery;
    private Long deliveryMethodNo;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String
            dname1,
            dname2,
            dstreet,
            dcity,
            dcountry;
    private Integer dpostalCode;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String name1,
            name2,
            street,
            city,
            country;
    private Integer postalCode;

    @NotEmpty
    @Valid
    private List<ItemDto> items;
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String paymentConditions;
    @JsonProperty("customerId")
    private Long customerNumber;

    /**
     * ContactInformation
     */
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String contact1, contact2, contact3, contact4;
    private String mark;
    private String saleRepresentative;
    private String valueAddedTaxIdNo;
    private String vendorNumber;

    /**
     * CustomerDetails
     * 
     * @return
     */

    public ContactInformation getContactInformation() {
        ContactInformation ci = new ContactInformation();
        ci.setContact1(contact1);
        ci.setContact2(contact2);
        ci.setContact2(contact3);
        ci.setContact2(contact4);
        return ci;
    }

    public void setContactInformation(ContactInformation ci) {
        contact1 = ci.getContact1();
        contact2 = ci.getContact2();
        contact3 = ci.getContact3();
        contact4 = ci.getContact4();
    }

    public CustomerDetails getCustomerDetails() {
        CustomerDetails cd = new CustomerDetails();
        cd.setContactInformation(getContactInformation());
        cd.setMark(mark);
        cd.setPaymentConditions(paymentConditions);
        cd.setSaleRepresentative(saleRepresentative);
        cd.setVatIdNo(valueAddedTaxIdNo);
        cd.setVendorNumber(vendorNumber);
        return cd;
    }

    public void setCustomerDetails(CustomerDetails cd) {
        setContactInformation(cd.getContactInformation());
        setMark(cd.getMark());
        setPaymentConditions(cd.getPaymentConditions());
        setSaleRepresentative(cd.getSaleRepresentative());
        setValueAddedTaxIdNo(cd.getVatIdNo());
        setVendorNumber(cd.getVendorNumber());
    }

    public ConfirmParameter(String orderNumber, String confirmNumber,
            LocalDate expectedDelivery, Long deliveryMethodNoNumber, Address shippingAddress,
            Address invoiceAddress, List<ItemDto> items) {
        this.orderNumber = orderNumber;
        this.confirmNumber = confirmNumber;
        this.expectedDelivery = expectedDelivery;
        this.deliveryMethodNo = deliveryMethodNoNumber;
        setShippingAddress(shippingAddress);
        setInvoiceAddress(invoiceAddress);
        this.items = items;
    }

    public void setShippingAddress(Address sa) {
        dname1 = sa.getName1();
        dname2 = sa.getName2();
        dcity = sa.getCity();
        dcountry = sa.getCountry().getName();
        dstreet = sa.getStreet();
        dpostalCode = sa.getPostalCode();
    }

    public void setInvoiceAddress(Address sa) {
        name1 = sa.getName1();
        name2 = sa.getName2();
        city = sa.getCity();
        country = sa.getCountry().getName();
        street = sa.getStreet();
        postalCode = sa.getPostalCode();
    }

    public Address getShippingAddress() {
        Address a = new Address();
        a.setCity(dcity);
        a.setCountry(Country.map(dcountry));
        a.setName1(dname1);
        a.setName2(dname2);
        a.setPostalCode(dpostalCode);
        a.setStreet(dstreet);
        return a;
    }

    public Address getInvoiceAddress() {
        Address a = new Address();
        a.setCity(city);
        a.setCountry(Country.map(country));
        a.setName1(name1);
        a.setName2(name2);
        a.setPostalCode(postalCode);
        a.setStreet(street);
        return a;
    }

    public ConfirmParameter() {}

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getConfirmNumber() {
        return confirmNumber;
    }

    public void setConfirmNumber(String confirmNumber) {
        this.confirmNumber = confirmNumber;
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

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    public String getPaymentConditions() {
        return paymentConditions;
    }

    public void setPaymentConditions(String paymentConditions) {
        this.paymentConditions = paymentConditions;
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
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

    public Integer getDpostalCode() {
        return dpostalCode;
    }

    public void setDpostalCode(Integer dpostalCode) {
        this.dpostalCode = dpostalCode;
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

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

}
