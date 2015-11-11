package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class InvoiceToDtoConversionService {

	@Autowired
	ReportToDtoConversionService reportToDtoConversionService;

	@Autowired
	PurchaseAgreementService purchaseAgreementService;

	@Transactional(readOnly = true)
	public ReportDto toDto(Invoice report) {
		ReportDto dto = reportToDtoConversionService.toDto(report);
		dto.shippingSpecific_shippingCosts = report.getShippingCosts();
		dto.orderConfirmationSpecific_paymentConditions = purchaseAgreementService.retrieveSingle(report).getPaymentConditions();
		dto.invoiceSpecific_billing = report.getBilling();
		return dto;
	}

}
