package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.Collection;

import de.switajski.priebes.flexibleorders.domain.embeddable.AgreementDetails;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;

public class ExtInfoTableParameter {
    public CustomerDetails customerDetails;
    public String expectedDelivery;
    public AgreementDetails agreementDetails;
    public String date;
    public String customerNo;
    public Collection<String> orderNumbers, orderConfirmationNumbers, orderAgreementNumbers, deliveryNotesNumbers, invoiceNumbers, creditNoteNumbers;
    public String billing;

    public ExtInfoTableParameter(
            CustomerDetails details,
            String expectedDelivery,
            AgreementDetails agreeDet,
            String date,
            String customerNo,
            Collection<String> orderNumbers) {
        this.customerDetails = details;
        this.expectedDelivery = expectedDelivery;
        this.agreementDetails = agreeDet;
        this.date = date;
        this.customerNo = customerNo;
        this.orderNumbers = orderNumbers;
    }

    public ExtInfoTableParameter() {}
}
