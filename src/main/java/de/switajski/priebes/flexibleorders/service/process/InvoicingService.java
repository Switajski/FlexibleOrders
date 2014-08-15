package de.switajski.priebes.flexibleorders.service.process;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.application.ShippingCostsCalculator;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class InvoicingService {

	@Autowired
	private ReportRepository reportRepo;
	
	@Autowired
	private ReportItemRepository reportItemRepo;
	
	@Autowired
	private ItemDtoConverterService itemDtoConverterService;
	
	@Transactional
	public Invoice invoice(String invoiceNumber, String paymentConditions,
			Date created, List<ItemDto> shippingItemDtos, String billing) {
		if (reportRepo.findByDocumentNumber(invoiceNumber) != null)
			throw new IllegalArgumentException("Rechnungsnr. existiert bereits");

		Invoice invoice = new Invoice(invoiceNumber, paymentConditions, null);
		invoice.setBilling(billing);

		Order order = null;
		Address invoiceAddress = null;
		for (ItemDto entry : shippingItemDtos) {
			ReportItem shipEventToBeInvoiced = reportItemRepo.findOne(entry.getId());

			OrderItem orderItemToBeInvoiced = shipEventToBeInvoiced
					.getOrderItem();

			ServiceHelper.validateQuantity(entry.getQuantityLeft(), shipEventToBeInvoiced);

			// validate addresses - DRY at deliver method
			Address temp = DeliveryHistory.createFrom(orderItemToBeInvoiced).getShippingAddress();
			if (invoiceAddress == null)
				invoiceAddress = temp;
			else if (!invoiceAddress.equals(temp))
				throw new IllegalStateException("AB-Positionen haben unterschiedliche Lieferadressen");

			invoice.setInvoiceAddress(invoiceAddress);
			
			invoice.addItem(new InvoiceItem(
					invoice,
					shipEventToBeInvoiced.getOrderItem(),
					entry.getQuantityLeft(), // TODO: GUI sets the quantity to
												// this nonsense place
					new Date()));

			if (order == null)
				order = orderItemToBeInvoiced.getOrder();
		}

		invoice.setShippingCosts(new ShippingCostsCalculator()
				.calculate(itemDtoConverterService
						.convertToShippingItems(shippingItemDtos)));
		invoice.setCreated((created == null) ? new Date() : created); 
		// TODO: refactor DRY!
		invoice.setCustomerNumber(order.getCustomer().getCustomerNumber());

		return reportRepo.save(invoice);
	}
}
