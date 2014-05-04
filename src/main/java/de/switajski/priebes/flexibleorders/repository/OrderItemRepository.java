package de.switajski.priebes.flexibleorders.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {

	@Query("SELECT oi from OrderItem oi where oi.customerOrder.customer = ?1")
	Page<OrderItem> findByCustomer(Customer customer, Pageable pageable);

	@Query("SELECT oi from OrderItem oi where oi.customerOrder.customer = ?1")
	List<OrderItem> findByCustomer(Customer customer);

	@Query("SELECT oi from OrderItem oi where oi.customerOrder.orderNumber = ?1")
	List<OrderItem> findByOrderNumber(String orderNumber);

}
