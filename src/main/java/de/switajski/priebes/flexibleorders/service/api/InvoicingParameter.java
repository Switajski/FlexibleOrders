package de.switajski.priebes.flexibleorders.service.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.switajski.priebes.flexibleorders.json.LocalDateDeserializer;
import de.switajski.priebes.flexibleorders.json.LocalDateSerializer;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class InvoicingParameter {
    @NotNull
    private String invoiceNumber;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate created;
    @Valid
    @NotEmpty
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
            LocalDate created,
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

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
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
