package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementService;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class OrderConfirmationToDtoConversionService {

	@Autowired
	ReportToDtoConversionService reportToDtoConversionService;
	@Autowired
	PurchaseAgreementService puchaseAgreementService;

	@Transactional(readOnly = true)
	public ReportDto toDto(OrderConfirmation orderConfirmation) {
		ReportDto dto = reportToDtoConversionService.toDto(orderConfirmation);
		if (orderConfirmation.isAgreed())
		    dto.orderConfirmationNumber = orderConfirmation.getOrderAgreementNumber();
		dto.orderConfirmationSpecific_paymentConditions = puchaseAgreementService.retrieveSingle(orderConfirmation.getItems()).getPaymentConditions();
		return dto;
	}

}
