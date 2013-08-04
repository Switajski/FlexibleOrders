package de.switajski.priebes.flexibleorders.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.reference.Status;

public interface ItemRepository<T extends Item> {
	List<T> findByOrderNumber(Long orderNumber);
	Page<T> findByOrderNumber(Long orderNumber, Pageable pageable);
	
	Page<T> findByOrderConfirmationNumber(Long OrderConfirmationNumber, Pageable pageable);
	List<T> findByOrderConfirmationNumber(Long OrderConfirmationNumber);
	
	Page<T> findByInvoiceNumber(Long InvoiceNumber, Pageable pageable);
	List<T> findByInvoiceNumber(Long InvoiceNumber);
	
	Page<T> findByAccountNumber(Long AccountNumber, Pageable pageable);
	List<T> findByAccountNumber(Long AccountNumber);
	
	List<T> findByCustomer(Customer customer);
	Page<T> findByCustomer(Customer customer, Pageable pageable);
	
	List<T> findByCustomerAndStatus(Customer customer, Status status);
	Page<T> findByCustomerAndStatus(Customer customer, Status status, Pageable pageable);
	
	List<T> findByStatus(Status status);
	Page<T> findByStatus(Status status, Pageable pageable);
	
}
