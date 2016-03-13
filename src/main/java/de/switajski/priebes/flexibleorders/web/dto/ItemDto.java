package de.switajski.priebes.flexibleorders.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;

import de.switajski.priebes.flexibleorders.json.EmptyStringStripToNullDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.json.LocalDateDeserializer;
import de.switajski.priebes.flexibleorders.json.LocalDateSerializer;
import de.switajski.priebes.flexibleorders.json.ProductTypeDeserializer;
import de.switajski.priebes.flexibleorders.reference.ProductType;

/**
 * Data Transfer Object for ExtJs GUI </br>
 * Build on <code>BestellpositionData</code>, which is written in JavaScript.
 *
 * @author Marek Switajski
 *
 */
@JsonAutoDetect
public class ItemDto {

    private Long id;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date created;
    @NotNull
    private String product;
    private Long customer,
            customerNumber;

    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String productName,
            customerName,
            orderNumber,
            documentNumber,
            invoiceNumber,
            receiptNumber,
            deliveryNotesNumber,
            orderConfirmationNumber,
            orderAgreementNumber,
            trackNumber,
            packageNumber,
            paymentConditions;

    private Integer quantity;
    @Min(1)
    private Integer quantityLeft;

    @JsonDeserialize(using = ProductTypeDeserializer.class)
    private ProductType productType;

    @DecimalMin("0")
    @NotNull
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal priceNet;

    private boolean shareHistory;

    private String status;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate expectedDelivery;

    private boolean agreed = true;

    private boolean pending = false;

    /**
     * Indicates, that this item should be delivered off the record. Off the
     * record means, that no purchase agreement for this items exists and the
     * item should be delivered nevertheless.
     */
    private boolean offTheRecord = false;

    public boolean isOffTheRecord() {
        return offTheRecord;
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
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ItemDto other = (ItemDto) obj;
        if (id == null) {
            if (other.id != null) return false;
        }
        else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + quantityLeft + " x " + productName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getDeliveryNotesNumber() {
        return deliveryNotesNumber;
    }

    public void setDeliveryNotesNumber(String deliveryNotesNumber) {
        this.deliveryNotesNumber = deliveryNotesNumber;
    }

    public String getOrderConfirmationNumber() {
        return orderConfirmationNumber;
    }

    public void setOrderConfirmationNumber(String orderConfirmationNumber) {
        this.orderConfirmationNumber = orderConfirmationNumber;
    }

    public String getOrderAgreementNumber() {
        return orderAgreementNumber;
    }

    public void setOrderAgreementNumber(String orderAgreementNumber) {
        this.orderAgreementNumber = orderAgreementNumber;
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

    public String getPaymentConditions() {
        return paymentConditions;
    }

    public void setPaymentConditions(String paymentConditions) {
        this.paymentConditions = paymentConditions;
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

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public BigDecimal getPriceNet() {
        return priceNet;
    }

    public void setPriceNet(BigDecimal priceNet) {
        this.priceNet = priceNet;
    }

    public boolean isShareHistory() {
        return shareHistory;
    }

    public void setShareHistory(boolean shareHistory) {
        this.shareHistory = shareHistory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(LocalDate expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public void setOffTheRecord(boolean offTheRecord) {
        this.offTheRecord = offTheRecord;
    }

}
