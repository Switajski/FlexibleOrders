package de.switajski.priebes.flexibleorders.web.itextpdf.parameter;

import java.util.Collection;

import de.switajski.priebes.flexibleorders.domain.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.ExpectedDeliveryStringCreator;

public class ExtInfoTableParameter {
	public String saleRepresentative, mark;
    public String expectedDelivery;
    public String date;
    public String customerNo;
    public Collection<String> orderNumbers, orderConfirmationNumbers, orderAgreementNumbers, deliveryNotesNumbers, invoiceNumbers, creditNoteNumbers;
    public String billing;
	public ContactInformation contactInformation;
	public String vendorNumber;
	public String vatIdNo;
	public DeliveryMethod deliveryMethod;
	public DeliveryMethod shippingAddress;

    public ExtInfoTableParameter(
            String expectedDelivery,
            String date,
            String customerNo,
            Collection<String> orderNumbers) {
        this.expectedDelivery = expectedDelivery;
        this.date = date;
        this.customerNo = customerNo;
        this.orderNumbers = orderNumbers;
    }

    public ExtInfoTableParameter() {}

	public ExtInfoTableParameter(ReportDto report) {
		creditNoteNumbers = report.related_creditNoteNumbers;
		orderNumbers = report.related_orderNumbers;
		orderAgreementNumbers = report.related_orderAgreementNumbers;
		deliveryNotesNumbers = report.related_deliveryNotesNumbers;
		invoiceNumbers = report.related_invoiceNumbers;
		
        this.expectedDelivery = ExpectedDeliveryStringCreator.createDeliveryWeekString(
                report.shippingSpecific_expectedDelivery, report.shippingSpecific_expectedDeliveryDateDeviates);
        this.date = report.created.toString();
        this.customerNo = report.customerNumber.toString();
	}
}
