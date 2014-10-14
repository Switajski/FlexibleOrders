package de.switajski.priebes.flexibleorders.domain.embeddable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import de.switajski.priebes.flexibleorders.json.JsonJodaLocalDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonJodaLocalDateSerializer;

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

    @JsonSerialize(using = JsonJodaLocalDateSerializer.class)
    public LocalDate getExpectedDelivery() {
        return new LocalDate(expectedDelivery);
    }

    @JsonDeserialize(using = JsonJodaLocalDateDeserializer.class)
    public void setExpectedDelivery(LocalDate expectedDelivery) {
        if (expectedDelivery == null)
            this.expectedDelivery = null;
        else
            this.expectedDelivery = new java.sql.Date(expectedDelivery.toDateTimeAtStartOfDay().toDate().getTime());
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
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }
    
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
