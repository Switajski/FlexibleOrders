package de.switajski.priebes.flexibleorders.web.itextpdf.parameter;

import java.util.Collection;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.ExpectedDeliveryStringCreator;

public class ExtInfoTableParameter {
	public String saleRepresentative, mark;
    public String expectedDelivery;
    public String date;
    public String customerNo;
    public Collection<String> orderNumbers, orderAgreementNumbers, orderConfirmationNumbers, deliveryNotesNumbers, invoiceNumbers, creditNoteNumbers;
    public String billing;
	public ContactInformation contactInformation;
	public String vendorNumber;
	public String vatIdNo;
	public DeliveryMethod deliveryMethod;
	public Address shippingAddress;

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
	    saleRepresentative = report.customerSpecific_saleRepresentative;
	    mark = report.customerSpecific_mark;
	    date = report.created.toString();
	    customerNo = report.customerNumber.toString();
	    
	    orderNumbers = report.related_orderNumbers;
	    creditNoteNumbers = report.related_creditNoteNumbers;
		orderConfirmationNumbers = report.related_orderConfirmationNumbers;
		deliveryNotesNumbers = report.related_deliveryNotesNumbers;
		invoiceNumbers = report.related_invoiceNumbers;
		
		billing = report.invoiceSpecific_billing;
		contactInformation = report.customerSpecific_contactInformation;
		vendorNumber = report.customerSpecific_vendorNumber;
		vatIdNo = report.customerSpecific_vatIdNo;
		deliveryMethod = report.shippingSpecific_deliveryMethod;
		shippingAddress = report.shippingSpecific_shippingAddress;
		
        this.expectedDelivery = ExpectedDeliveryStringCreator.createDeliveryWeekString(
                report.shippingSpecific_expectedDelivery, report.shippingSpecific_expectedDeliveryDateDeviates);
	}
}
