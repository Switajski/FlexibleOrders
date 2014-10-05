package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class OrderConfirmationToDtoConversionService {

	@Autowired
	ReportToDtoConversionService reportToDtoConversionService;

	@Transactional(readOnly = true)
	public ReportDto toDto(OrderConfirmation orderConfirmation) {
		return reportToDtoConversionService.toDto(orderConfirmation);
	}

}
