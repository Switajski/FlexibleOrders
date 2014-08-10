package de.switajski.priebes.flexibleorders.service.process;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class DeliveryService {

	@Autowired
	private ReportRepository reportRepo;
	@Autowired
	private ReportItemRepository reportItemRepo;
	
	@Transactional
	public DeliveryNotes deliver(String deliveryNotesNumber,
			String trackNumber, String packageNumber,
			Amount shipment, Date created, List<ItemDto> confirmEvents) {
		if (reportRepo.findByDocumentNumber(deliveryNotesNumber) != null)
			throw new IllegalArgumentException("Rechnungsnr. existiert bereits");

		DeliveryNotes deliveryNotes = new DeliveryNotes(
				deliveryNotesNumber,
				null,
				shipment);
		deliveryNotes.setCreated(created== null ? new Date() : created);
		
		Order firstOrder = null;
		
		Address shippedAddress = null;
		for (ItemDto entry : confirmEvents) {
			ReportItem confirmEventToBeDelivered = reportItemRepo
					.findOne(entry.getId());
			OrderItem orderItemToBeDelivered = confirmEventToBeDelivered
					.getOrderItem();

			ServiceHelper.validateQuantity(entry, confirmEventToBeDelivered);

			deliveryNotes.addItem(new ShippingItem(
					deliveryNotes,
					orderItemToBeDelivered,
					entry.getQuantityLeft(), // TODO: GUI sets
												// quanitityToDeliver at this
												// nonsense parameter
					new Date()));
			
			//validate addresses DRY!
			Address temp = DeliveryHistory.createFrom(orderItemToBeDelivered).getShippingAddress();
			if (shippedAddress == null)
				shippedAddress = temp;
			else if (!shippedAddress.equals(temp))
				throw new IllegalStateException("AB-Positionen haben unterschiedliche Lieferadressen");
			
			deliveryNotes.setShippedAddress(shippedAddress);

			if (firstOrder == null)
				firstOrder = orderItemToBeDelivered.getOrder();
		}

		// TODO:Refactor: DRY!
		deliveryNotes.setCustomerNumber(firstOrder
				.getCustomer()
				.getCustomerNumber());
		return reportRepo.save(deliveryNotes);
	}
	
}
