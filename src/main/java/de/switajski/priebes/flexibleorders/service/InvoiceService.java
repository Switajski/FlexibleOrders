package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Invoice;

public interface InvoiceService {

	List<Invoice> findByCustomer(Customer customer);
	
	List<Long> findInvoiceNumbersLike(Long invoiceNumber);
}
