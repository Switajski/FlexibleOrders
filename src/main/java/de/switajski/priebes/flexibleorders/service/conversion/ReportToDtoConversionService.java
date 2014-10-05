package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class ReportToDtoConversionService {

	@Transactional(readOnly=true)
	public ReportDto toDto(Report report){
		ReportDto dto = new ReportDto();
		dto.created = report.getCreated();
		dto.customerNumber = report.getCustomerNumber();
		dto.documentNumber = report.getDocumentNumber();
		dto.items = report.getItems();
		dto.deliveryHistory = DeliveryHistory.of(report);
		return dto;
	}
	
}
