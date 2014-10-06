package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
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
                .getAmountsTimesQuantity(report));
		dto.purchaseAgreement = purchaseAgreementService.retrieveOne(report.getItems());
		dto.customerDetails = report.getCustomerDetails();
		return dto;
	}
	
}
