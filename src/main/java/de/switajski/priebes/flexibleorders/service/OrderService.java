package de.switajski.priebes.flexibleorders.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.report.Order;

/**
 * Orders are reports. Consequently read-only.
 * @author Marek
 *
 */
public interface OrderService {

	Long countAll();
	
	Page<Long> getOrderNumbersByCustomer(Customer customer, Pageable pageable);
	
	Page<Order> findAll(Pageable pageable);
	
	Page<Order> findByCustomer(Customer customer);
	
	Page<Order> findByCustomer(Customer customer, Pageable pageable);
	
	Order find(Long orderNumber);
	
}
