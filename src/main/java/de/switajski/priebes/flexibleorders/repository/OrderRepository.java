package de.switajski.priebes.flexibleorders.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order>{

	Order findByOrderNumber(String orderNumber);

	Page<Order> findByCustomer(Customer customer, Pageable pageable);

	List<Order> findByOrderNumberLike(String orderNumber);

	List<Order> findByCustomer(Customer customer);
	
	@Query("SELECT distinct(o) from Order o join o.items oi where oi.reportItems is empty")
	Page<Order> findAllToBeConfirmed(Pageable pageable);
	
	@Query("SELECT distinct(o) from Order o join o.items oi where oi.reportItems is empty "
			+ "and o.customer = ?1")
	Page<Order> findAllToBeConfirmedByCustomer(Customer customer, Pageable pageable);
	
}
