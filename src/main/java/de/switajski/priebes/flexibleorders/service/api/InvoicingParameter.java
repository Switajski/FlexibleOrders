package de.switajski.priebes.flexibleorders.service.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class InvoicingParameter {
    private String invoiceNumber;
    private Date created;
    private List<ItemDto> items;
    private String billing;
    @JsonProperty("customerId")
    private Long customerNumber;
    private BigDecimal discountRate;
    private String discountText;

    /**
     * Constructor with mandatory fields.
     *
     * @param invoiceNumber
     * @param paymentConditions
     * @param created
     * @param shippingItemDtos
     */
    public InvoicingParameter(String invoiceNumber,
            Date created,
            List<ItemDto> shippingItemDtos) {
        this.invoiceNumber = invoiceNumber;
        this.created = created;
        this.items = shippingItemDtos;
    }

    public InvoicingParameter() {}

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public String getDiscountText() {
        return discountText;
    }

    public void setDiscountText(String discountText) {
        this.discountText = discountText;
    }
}
