package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.Collection;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.service.CustomerDetailsService;
import de.switajski.priebes.flexibleorders.service.DeliveryMethodService;
import de.switajski.priebes.flexibleorders.service.ExpectedDeliveryService;
import de.switajski.priebes.flexibleorders.service.InvoicingAddressService;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.service.ShippingAddressService;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class ReportToDtoConversionService {

	@Autowired
	ExpectedDeliveryService expectedDeliveryService;
	@Autowired
	InvoicingAddressService invoicingAddressService;
	@Autowired
	ShippingAddressService shippingAddressService;
	@Autowired
	CustomerDetailsService customerDetailsService;
	@Autowired
	PurchaseAgreementService purchaseAgreementService;
	@Autowired
	DeliveryMethodService deliveryMethodService;
	@Autowired
	CustomerDetailsService customerService;

	@Transactional(readOnly=true)
	public ReportDto toDto(Report report){
		ReportDto dto = new ReportDto();
		dto.created = report.getCreated();
		dto.documentNumber = report.getDocumentNumber();
		dto.items = report.getItems();
		
		DeliveryHistory dh = DeliveryHistory.of(report);
		dto.related_creditNoteNumbers = dh.getCreditNoteNumbers();
		dto.related_deliveryNotesNumbers = dh.getDeliveryNotesNumbers();
		dto.related_invoiceNumbers = dh.getInvoiceNumbers();
		dto.related_orderAgreementNumbers = dh.getOrderAgreementNumbers();
		dto.related_orderNumbers = dh.getOrderNumbers();
		dto.related_orderConfirmationNumbers = dh.getOrderConfirmationNumbers();

		Collection<Customer> customers = report.getCustomers();
		if (report.getCustomers().size() > 1){
			throw new IllegalStateException("Mehr als einen Kunden f"+Unicode.uUml+"r gegebene Positionen gefunden");
		} else if (report.getCustomers().size() == 1){
			Customer customer = customers.iterator().next();
			dto.customerFirstName = customer.getFirstName();
			dto.customerLastName = customer.getLastName();
			dto.customerEmail = customer.getEmail();
			dto.customerPhone = customer.getPhone();
			dto.customerNumber = customer.getCustomerNumber();
		}
		
		dto.headerAddress = retrieveInvoicingAddress(report);
		dto.shippingSpecific_shippingAddress = retrieveShippingAddress(report);
		mapCustomerDetails(dto, retrieveCustomerDetails(report));
		dto.shippingSpecific_expectedDelivery = retrieveExpectedDelivery(report);
		dto.shippingSpecific_deliveryMethod = retrieveDeliveryMethod(report);

		dto.vatRate = 0.19d; //TODO: implement VAT-Service
		
		dto.netGoods = AmountCalculator.sum(AmountCalculator
				.getAmountsTimesQuantity(report.getItems()));

		return dto;
	}

    private DeliveryMethod retrieveDeliveryMethod(Report report) {
        DeliveryMethod dm = null;
		Set<DeliveryMethod> deliveryMethods = deliveryMethodService.retrieve(report.getItems());
		if (deliveryMethods.size() > 1)
			throw new ContradictoryPurchaseAgreementException("Mehr als eine Zustellungsart f"+Unicode.uUml+"r gegebene Positionen gefunden");
		else if (deliveryMethods.size() == 1){
			dm = deliveryMethods.iterator().next();
		}
        return dm;
    }

    private LocalDate retrieveExpectedDelivery(Report report) {
        Set<LocalDate> eDates = expectedDeliveryService.retrieve(report.getItems());
		LocalDate expectedDelivery = null;
		if (eDates.size() > 1)
			throw new ContradictoryPurchaseAgreementException("Mehr als ein Lieferdatum f"+Unicode.uUml+"r gegebene Positionen gefunden");
		else if (eDates.size() == 1)
			expectedDelivery = eDates.iterator().next();
        return expectedDelivery;
    }

    private CustomerDetails retrieveCustomerDetails(Report report) {
        CustomerDetails customerDetails = null;
		Set<CustomerDetails> customerDetailss = customerDetailsService.retrieve(report.getItems());
		if (customerDetailss.size() > 1)
			throw new IllegalStateException("Widerspr"+Unicode.uUml+"chliche Kundenstammdaten f"+Unicode.uUml+"r gegebene Positionen gefunden");
		else if (customerDetailss.size() == 1){
		    customerDetails = customerDetailss.iterator().next();
		}
        return customerDetails;
    }

    private void mapCustomerDetails(ReportDto dto, CustomerDetails det) {
        if (det != null) {
            dto.customerSpecific_contactInformation = det.getContactInformation();
            dto.customerSpecific_mark = det.getMark();
            dto.customerSpecific_saleRepresentative = det.getSaleRepresentative();
            dto.customerSpecific_vatIdNo = det.getVatIdNo();
            dto.customerSpecific_vendorNumber = det.getVendorNumber();
        }
    }

    private Address retrieveShippingAddress(Report report) {
        Address shippingAddress = null;
		Set<Address> shippingAddresses = shippingAddressService.retrieve(report.getItems());
		if (shippingAddresses.size() > 1)
			throw new ContradictoryPurchaseAgreementException("Mehr als eine Lieferadresse f"+Unicode.uUml+"r gegebene Positionen gefunden");
		else if (shippingAddresses.size() == 1)
			shippingAddress = shippingAddresses.iterator().next();
        return shippingAddress;
    }

    private Address retrieveInvoicingAddress(Report report) {
        Set<Address> invoicingAddresses = invoicingAddressService.retrieve(report.getItems());
		if (invoicingAddresses.size() > 1)
			throw new ContradictoryPurchaseAgreementException("Mehr als eine Rechungsaddresse f"+Unicode.uUml+"r gegebene Positionen gefunden");
		else if (invoicingAddresses.size() == 1){
			return invoicingAddresses.iterator().next();
		} 
        return null;
    }
}
