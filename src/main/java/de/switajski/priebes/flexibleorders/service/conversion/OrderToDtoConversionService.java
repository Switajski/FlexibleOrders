package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class OrderToDtoConversionService {

	@Transactional(readOnly=true)
	public ReportDto toDto(Order order){
		ReportDto dto = new ReportDto();
		dto.created = order.getCreated();
		dto.customerNumber = order.getCustomer().getCustomerNumber();
		dto.documentNumber = order.getOrderNumber();
		dto.invoiceAddress = order.getCustomer().getInvoiceAddress();
		dto.orderItems = order.getItems(); 
		dto.netGoods = AmountCalculator.sum(AmountCalculator
                .getAmountsTimesQuantity(order));
		dto.vatRate = order.getVatRate();
		return dto;
	}
	
}
