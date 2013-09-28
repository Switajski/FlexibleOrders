package de.switajski.priebes.flexibleorders.service;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

/**
 * @author Marek
 *
 */
public interface TransitionService {

	ShippingItem confirm(long orderNumber, 
			Product product, 
			int quantity, 
			boolean toSupplier, 
			long orderConfirmationNumber);
	ShippingItem deconfirm(long orderNumber, 
			Product product, 
			long orderConfirmationNumber);
	
	InvoiceItem deliver(long orderConfirmationNumber, 
			Product product, 
			int quantity, 
			long invoiceNumber, 
			String trackNumber, 
			String packageNumber);
	InvoiceItem withdraw(InvoiceItem invoiceItem);
	
	ArchiveItem complete(InvoiceItem invoiceItem, Long accountNumber);
	ArchiveItem decomplete(ArchiveItem archiveItem);
}
