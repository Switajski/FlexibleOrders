package de.switajski.priebes.flexibleorders.domain.embeddable;

import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.switajski.priebes.flexibleorders.application.DateUtils;
import de.switajski.priebes.flexibleorders.json.LocalDateDeserializer;
import de.switajski.priebes.flexibleorders.json.LocalDateSerializer;

@Embeddable
public class PurchaseAgreement {

    @AttributeOverrides({
            @AttributeOverride(name = "name1", column = @Column(name = "invoice_name1")),
            @AttributeOverride(name = "name2", column = @Column(name = "invoice_name2")),
            @AttributeOverride(name = "street", column = @Column(name = "invoice_street")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "invoice_postal_code")),
            @AttributeOverride(name = "city", column = @Column(name = "invoice_city")),
            @AttributeOverride(name = "country", column = @Column(name = "invoice_country"))
    })
    private Address invoiceAddress;

    @AttributeOverrides({
            @AttributeOverride(name = "name1", column = @Column(name = "shipping_name1")),
            @AttributeOverride(name = "name2", column = @Column(name = "shipping_name2")),
            @AttributeOverride(name = "street", column = @Column(name = "shipping_street")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "shipping_postal_code")),
            @AttributeOverride(name = "city", column = @Column(name = "shipping_city")),
            @AttributeOverride(name = "country", column = @Column(name = "shipping_country"))
    })
    private Address shippingAddress;

    @Embedded
    private DeliveryMethod deliveryMethod;

    @DateTimeFormat(style = "M-")
    private java.sql.Date expectedDelivery;

    @Column(name = "pa_customer_number")
    private Long customerNumber;

    @Column(name = "pa_paymentConditions")
    private String paymentConditions;

    /**
     * Copy constructor
     * 
     * @param pa
     */
    public PurchaseAgreement(PurchaseAgreement pa) {
        this.invoiceAddress = pa.getInvoiceAddress();
        this.shippingAddress = pa.getShippingAddress();
        this.setExpectedDelivery(pa.getExpectedDelivery());
        this.deliveryMethod = pa.getDeliveryMethod();
        this.paymentConditions = pa.getPaymentConditions();
        this.customerNumber = pa.getCustomerNumber();
    }

    public PurchaseAgreement() {}

    public void setExpectedDelivery(java.sql.Date expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public String getPaymentConditions() {
        return paymentConditions;
    }

    public Address getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(Address invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getExpectedDelivery() {
        if (expectedDelivery == null) return null;
        return DateUtils.asLocalDate(expectedDelivery);
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    public void setExpectedDelivery(LocalDate expectedDelivery) {
        if (expectedDelivery == null) this.expectedDelivery = null;
        else this.expectedDelivery = java.sql.Date.valueOf(expectedDelivery);
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public void setPaymentConditions(String paymentConditions) {
        this.paymentConditions = paymentConditions;
    }

}
