package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.report.Invoice;

/**
 * Orders are reports. Consequently read-only.
 * @author Marek
 *
 */
public interface InvoiceService extends CrudServiceAdapter<Invoice> {

	Page<Long> getInvoiceNumbersByCustomer(Customer customer, Pageable pageable);
	
	Page<Invoice> findAll(Pageable pageable);
	
	List<Invoice> findByCustomer(Customer customer);
	
	Page<Invoice> findByCustomer(Customer customer, Pageable pageable);
	
	Invoice find(Long orderNumber);
	
	List<Long> findInvoiceNumbersLike(Long orderNumber);
	
}
