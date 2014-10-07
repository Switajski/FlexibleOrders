package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
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
