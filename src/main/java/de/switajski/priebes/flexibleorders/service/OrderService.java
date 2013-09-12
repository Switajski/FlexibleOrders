package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.report.Order;

/**
 * Orders are reports. Consequently read-only.
 * @author Marek
 *
 */
public interface OrderService extends CrudServiceAdapter<Order> {

	Page<Long> getOrderNumbersByCustomer(Customer customer, Pageable pageable);
	
	Page<Order> findAll(Pageable pageable);
	
	List<Order> findByCustomer(Customer customer);
	
	Page<Order> findByCustomer(Customer customer, Pageable pageable);
	
	Order find(Long orderNumber);
	
	List<Long> findOrderNumbersLike(Long orderNumber);
	
}
