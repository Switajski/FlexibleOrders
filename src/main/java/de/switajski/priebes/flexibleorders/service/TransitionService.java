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
 * @author Marek
 *
 */
public interface TransitionService {

	List<ShippingItem> confirm(Customer customer, Product product, int quantity, boolean toSupplier, long orderConfirmationNumber);
	ShippingItem deconfirm(Customer customer, Product product, long orderConfirmationNumber);
	
	List<InvoiceItem> deliver(Customer customer, Product product, int quantity, long invoiceNumber, String trackNumber, String packageNumber);
	InvoiceItem withdraw(Customer customer, Product product, long invoiceNumber, int quantity);
	
	List<ArchiveItem> complete(Customer customer, Product product, int quantity, long accountNumber);
	ArchiveItem decomplete(Customer customer, Product product, long accountNumber);
}
