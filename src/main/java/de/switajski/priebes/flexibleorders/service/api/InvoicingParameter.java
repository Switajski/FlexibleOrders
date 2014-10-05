package de.switajski.priebes.flexibleorders.service.api;

import java.util.Date;
import java.util.List;

import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class InvoicingParameter {
    public String invoiceNumber;
    public String paymentConditions;
    public Date created;
    public List<ItemDto> shippingItemDtos;
    public String billing;
    public Long customerNumber;

    public InvoicingParameter(String invoiceNumber, String paymentConditions, Date created, List<ItemDto> shippingItemDtos, String billing) {
        this.invoiceNumber = invoiceNumber;
        this.paymentConditions = paymentConditions;
        this.created = created;
        this.shippingItemDtos = shippingItemDtos;
        this.billing = billing;
    }

    public InvoicingParameter() {
    }
}