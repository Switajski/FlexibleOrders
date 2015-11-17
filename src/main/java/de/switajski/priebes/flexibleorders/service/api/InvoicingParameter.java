package de.switajski.priebes.flexibleorders.service.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class InvoicingParameter {
    public String invoiceNumber;
    public Date created;
    public List<ItemDto> shippingItemDtos;
    public String billing;
    public Long customerNumber;
    public BigDecimal discountRate;
    public String discountText;

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
        this.shippingItemDtos = shippingItemDtos;
    }

    public InvoicingParameter() {}
}
