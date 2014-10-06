package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.service.CustomerDetailsService;
import de.switajski.priebes.flexibleorders.service.InvoicingAddressService;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class InvoiceToDtoConversionService {

	@Autowired
	private InvoicingAddressService invocingAddressService;

	@Autowired
	ReportToDtoConversionService reportToDtoConversionService;

	@Autowired
	CustomerDetailsService customerDetailsService;

	@Transactional(readOnly = true)
	public ReportDto toDto(Invoice report) {
		ReportDto dto = reportToDtoConversionService.toDto(report);
		dto.netGoods = AmountCalculator.sum(AmountCalculator
				.getAmountsTimesQuantity(report.getItems()));
		dto.vatRate = report.getVatRate();
		dto.invoiceSpecific_billing = report.getBilling();
		dto.invoiceSpecific_hasItemsWithDifferentCreationDates = hasShippingItemsWithDifferingCreatedDates(report);
		dto.invoiceSpecific_paymentConditions = report.getPaymentConditions();
		dto.shippingSpecific_shippingCosts = report.getShippingCosts();
		dto.vatRate = report.getVatRate();
		
		Set<Address> invoiceAdresses = invocingAddressService.retrieve(report.getItems());
		if (invoiceAdresses.size() == 0)
			throw new NotFoundException("Could not find an invoicing address for invoice!");
		if (invoiceAdresses.size() > 1)
			throw new ContradictoryPurchaseAgreementException("Several invoicing addresses for one onvoice found!");
		else dto.invoiceSpecific_invoiceAddress = invoiceAdresses.iterator().next();
		
		Set<CustomerDetails> customerDetailss = customerDetailsService.retrieve(report.getItems());
		if (customerDetailss.size() > 1)
			throw new ContradictoryPurchaseAgreementException("Found contradictory information about customer");

		CustomerDetails det = customerDetailss.iterator().next();
		dto.customerSpecific_contactInformation = det.getContactInformation();
		dto.customerSpecific_mark = det.getMark();
		dto.customerSpecific_saleRepresentative = det.getSaleRepresentative();
		dto.customerSpecific_vatIdNo = det.getVatIdNo();
		dto.customerSpecific_vendorNumber = det.getVendorNumber();
		
		return dto;
	}

	/**
	 * @param invoice
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean hasShippingItemsWithDifferingCreatedDates(Invoice invoice) {
		Set<ShippingItem> shippingItems = invoice.getItems().iterator().next()
				.getOrderItem().getShippingItems();
		Date firstDate = shippingItems.iterator().next().getCreated();
		for (ShippingItem si : shippingItems) {
			if (!DateUtils.isSameDay(si.getDeliveryNotes().getCreated(),
					firstDate))
				return true;
		}
		return false;
	}

}
