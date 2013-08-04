package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.report.Order;

/**
 * Orders are reports. Consequently read-only.
 * @author Marek
 *
 */
public interface TransitionService {

	List<ShippingItem> confirm(Customer customer, Product product, int quantity, boolean toSupplier, long orderConfirmationNumber);
	
	List<InvoiceItem> deliver(Customer customer, Product product, int quantity, long invoiceNumber);
	
	List<ArchiveItem> complete(Customer customer, Product product, int quantity, long accountNumber);
}
