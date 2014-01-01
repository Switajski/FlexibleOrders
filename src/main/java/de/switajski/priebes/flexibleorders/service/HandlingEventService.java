package de.switajski.priebes.flexibleorders.service;

import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.PayedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;

public interface HandlingEventService {

	HandlingEvent cancel(Item item);

	HandlingEvent confirm(Item orderItemToConfirm, int quantity, ConfirmedSpecification confirmedSpec);
	HandlingEvent deconfirm(Item shippingItem);
	
	HandlingEvent deliver(Item shippingItemToDeliver, int quantity, ShippedSpecification shippingSpec);
	HandlingEvent withdraw(Item invoiceItem);
	
	HandlingEvent complete(Item invoiceItem, int quantity, PayedSpecification accountSpec);
	HandlingEvent decomplete(Item archiveItem);
	
}
