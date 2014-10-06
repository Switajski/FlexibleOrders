package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.service.CustomerDetailsService;
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
	
	@Transactional(readOnly=true)
	public ReportDto toDto(Report report){
		ReportDto dto = new ReportDto();
		dto.created = report.getCreated();
		dto.customerNumber = report.getCustomerNumber();
		dto.documentNumber = report.getDocumentNumber();
		dto.items = report.getItems();
		
		DeliveryHistory dh = DeliveryHistory.of(report);
		dto.related_creditNoteNumbers = dh.getCreditNoteNumbers();
		dto.related_deliveryNotesNumbers = dh.getDeliveryNotesNumbers();
		dto.related_invoiceNumbers = dh.getInvoiceNumbers();
		dto.related_orderAgreementNumbers = dh.getOrderAgreementNumbers();
		dto.related_orderNumbers = dh.getOrderNumbers();
		
		Set<PurchaseAgreement> purchaseAgreements = purchaseAgreementService.retrieve(report.getItems());
		if (purchaseAgreements.size() > 1)
			throw new ContradictoryPurchaseAgreementException("Mehr als ein Kaufvertrag f"+Unicode.uUml+"r gegebene Positionen gefunden");
		else if (purchaseAgreements.size() == 1){
			PurchaseAgreement pa = purchaseAgreements.iterator().next();
			dto.shippingSpecific_deliveryMethod = pa.getDeliveryMethod();
		}
		
		Set<Address> invoicingAddresses = invoicingAddressService.retrieve(report.getItems());
		if (invoicingAddresses.size() > 1)
			throw new ContradictoryPurchaseAgreementException("Mehr als eine Rechungsaddresse f"+Unicode.uUml+"r gegebene Positionen gefunden");
		else if (invoicingAddresses.size() == 1)
			dto.invoiceSpecific_invoiceAddress = invoicingAddresses.iterator().next();  
		
		Set<Address> shippingAddresses = shippingAddressService.retrieve(report.getItems());
		if (shippingAddresses.size() > 1)
			throw new ContradictoryPurchaseAgreementException("Mehr als eine Lieferadresse f"+Unicode.uUml+"r gegebene Positionen gefunden");
		else if (shippingAddresses.size() == 1)
			dto.shippingSpecific_shippingAddress = shippingAddresses.iterator().next();
		
		Set<CustomerDetails> customerDetailss = customerDetailsService.retrieve(report.getItems());
		if (customerDetailss.size() > 1)
			throw new IllegalStateException("Widerspr"+Unicode.uUml+"chliche Kundenstammdaten f"+Unicode.uUml+"r gegebene Positionen gefunden");
		else if (customerDetailss.size() == 1){
			CustomerDetails det = customerDetailss.iterator().next();
			dto.customerSpecific_contactInformation = det.getContactInformation();
			dto.customerSpecific_mark = det.getMark();
			dto.customerSpecific_saleRepresentative = det.getSaleRepresentative();
			dto.customerSpecific_vatIdNo = det.getVatIdNo();
			dto.customerSpecific_vendorNumber = det.getVendorNumber();
		}

		Set<LocalDate> eDates = expectedDeliveryService.retrieve(report.getItems());
		if (invoicingAddresses.size() > 1)
			throw new ContradictoryPurchaseAgreementException("Mehr als ein Lieferdatum f"+Unicode.uUml+"r gegebene Positionen gefunden");
		else if (invoicingAddresses.size() == 1)
			dto.shippingSpecific_expectedDelivery = eDates.iterator().next();
		
		dto.vatRate = 0.19d; //TODO: implement VAT-Service
		
		dto.netGoods = AmountCalculator.sum(AmountCalculator
				.getAmountsTimesQuantity(report.getItems()));

		return dto;
	}

}
