package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.service.CustomerDetailsService;
import de.switajski.priebes.flexibleorders.service.InvoicingAddressService;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class OrderConfirmationToDtoConversionService {

	@Autowired
	ReportToDtoConversionService reportToDtoConversionService;
	@Autowired
	InvoicingAddressService invocingAddressService;
	@Autowired
	CustomerDetailsService customerDetailsService;

	@Transactional(readOnly = true)
	public ReportDto toDto(OrderConfirmation orderConfirmation) {
		ReportDto dto = reportToDtoConversionService.toDto(orderConfirmation);
		dto.netGoods = AmountCalculator.sum(AmountCalculator
				.getAmountsTimesQuantity(orderConfirmation));
		dto.expectedDelivery = orderConfirmation.getPurchaseAgreement().getExpectedDelivery();
		dto.vatRate = orderConfirmation.getVatRate();
		
		dto.invoiceAddress = orderConfirmation.getPurchaseAgreement().getInvoiceAddress();
		
		Set<CustomerDetails> customerDetailss = customerDetailsService.retrieve(orderConfirmation.getItems());
		if (customerDetailss.isEmpty())
			dto.customerDetails = null;
		else if (customerDetailss.size() > 1)
			throw new ContradictoryPurchaseAgreementException("Found contradictory information about customer");
		else dto.customerDetails = customerDetailss.iterator().next();

		return dto;
	}

}
