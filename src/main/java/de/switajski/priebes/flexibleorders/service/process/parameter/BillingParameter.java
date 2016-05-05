package de.switajski.priebes.flexibleorders.service.process.parameter;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.switajski.priebes.flexibleorders.json.EmptyStringStripToNullDeserializer;
import de.switajski.priebes.flexibleorders.json.LocalDateDeserializer;
import de.switajski.priebes.flexibleorders.json.LocalDateSerializer;
import de.switajski.priebes.flexibleorders.validation.ReportNumber;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class BillingParameter {
    @ReportNumber(shouldExist = true)
    @NotNull
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String invoiceNumber;
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    private String receiptNumber;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private Date date;
    private List<ItemDto> items;

    public BillingParameter() {}

    public BillingParameter(String invoiceNumber, String receiptNumber, Date date) {
        this.invoiceNumber = invoiceNumber;
        this.receiptNumber = receiptNumber;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }
}
