package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class OrderAgreementToDtoConversionService {

	@Autowired
	private ReportToDtoConversionService reportToDtoConversionService;
	
	@Transactional(readOnly=true)
	public ReportDto toDto(OrderAgreement report){
		return reportToDtoConversionService.toDto(report);
	}
	
}
