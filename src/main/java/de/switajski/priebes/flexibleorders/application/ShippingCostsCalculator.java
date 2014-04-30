package de.switajski.priebes.flexibleorders.application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

public class ShippingCostsCalculator {

	public Amount calculate(Set<ShippingItem> shippingItems) {
		Amount summedShippingCosts = new Amount();
		for (DeliveryNotes dn : retrieveDeliveryNotesWithShippingCosts(shippingItems)) {
			summedShippingCosts = summedShippingCosts
					.add(dn.getShippingCosts());
		}
		return summedShippingCosts;
	}

	private Set<DeliveryNotes> retrieveDeliveryNotesWithShippingCosts(
			Set<ShippingItem> shippingItemDtos) {
		// using HashMap with documentNumber to not sum up shipping costs from
		// same document
		HashMap<String, DeliveryNotes> deliveryNotes = new HashMap<String, DeliveryNotes>();

		for (ShippingItem entry : shippingItemDtos) {
			for (ReportItem shippingItem : entry.getOrderItem().getShippingItems()) {
				DeliveryNotes dn = (DeliveryNotes) shippingItem.getReport();
				if (dn.hasShippingCosts())
					deliveryNotes.put(dn.getDocumentNumber(), dn);
			}
		}
		return new HashSet<DeliveryNotes>(deliveryNotes.values());
	}
}
