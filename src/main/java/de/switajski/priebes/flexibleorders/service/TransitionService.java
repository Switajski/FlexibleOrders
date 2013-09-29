package de.switajski.priebes.flexibleorders.service;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.domain.parameter.AccountParameter;
import de.switajski.priebes.flexibleorders.domain.parameter.ConfirmationParameter;
import de.switajski.priebes.flexibleorders.domain.parameter.ShippingParameter;

/**
 * A Transition is an action in the order process. Therefore specific items can be
 * removed or added. 
 * @author Marek Switajski
 *
 */
public interface TransitionService {

	ShippingItem confirm(OrderItem orderItemToConfirm, ConfirmationParameter confirmationParameter);
	ShippingItem deconfirm(ShippingItem shippingItem);
	
	InvoiceItem deliver(ShippingItem shippingItemToDeliver, ShippingParameter shippingParameter);
	InvoiceItem withdraw(InvoiceItem invoiceItem);
	
	ArchiveItem complete(InvoiceItem invoiceItem, AccountParameter accountParameter);
	ArchiveItem decomplete(ArchiveItem archiveItem);
}
