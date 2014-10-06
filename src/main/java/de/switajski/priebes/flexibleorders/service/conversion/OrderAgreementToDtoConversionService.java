package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class OrderAgreementToDtoConversionService {

	@Autowired
	ReportToDtoConversionService reportToDtoConversionService;
	@Autowired
	PurchaseAgreementService purchaseAgreementService;
		
	@Transactional(readOnly=true)
	public ReportDto toDto(OrderAgreement report){
		ReportDto dto = reportToDtoConversionService.toDto(report);
		dto.netGoods = AmountCalculator.sum(AmountCalculator
                .getAmountsTimesQuantity(report.getItems()));
		PurchaseAgreement pa = purchaseAgreementService.retrieveOne(report.getItems());
		dto.shippingSpecific_deliveryMethod = pa.getDeliveryMethod();
		dto.shippingSpecific_expectedDelivery = pa.getExpectedDelivery();
		dto.invoiceSpecific_invoiceAddress = pa.getInvoiceAddress();
		dto.shippingSpecific_shippingAddress = pa.getShippingAddress();
		dto.orderConfirmationNumber = report.getOrderConfirmationNumber();
		
		CustomerDetails det = report.getCustomerDetails();
		dto.customerSpecific_contactInformation = det.getContactInformation();
		dto.vatRate = report.getVatRate();
		dto.customerSpecific_mark = det.getMark();
		dto.customerSpecific_saleRepresentative = det.getSaleRepresentative();
		dto.customerSpecific_vatIdNo = det.getVatIdNo();
		dto.customerSpecific_vendorNumber = det.getVendorNumber();
		
		return dto;
	}
	
}
