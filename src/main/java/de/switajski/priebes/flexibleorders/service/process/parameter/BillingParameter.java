package de.switajski.priebes.flexibleorders.service.process.parameter;

import java.util.Date;

public class BillingParameter {
    public String invoiceNumber;
    public String receiptNumber;
    public Date date;

    public BillingParameter(String invoiceNumber, String receiptNumber, Date date) {
        this.invoiceNumber = invoiceNumber;
        this.receiptNumber = receiptNumber;
        this.date = date;
    }
}