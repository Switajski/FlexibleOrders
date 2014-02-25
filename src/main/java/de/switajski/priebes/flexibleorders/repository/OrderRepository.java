package de.switajski.priebes.flexibleorders.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;

public interface OrderRepository extends JpaRepository<FlexibleOrder, Long>, JpaSpecificationExecutor<FlexibleOrder>{

	FlexibleOrder findByOrderNumber(String orderNumber);

	Page<FlexibleOrder> findByCustomer(Customer customer, Pageable pageable);

	List<FlexibleOrder> findByOrderNumberLike(String orderNumber);

	List<FlexibleOrder> findByCustomer(Customer customer);
	
	@Query("SELECT distinct(o) from FlexibleOrder o join o.items oi where oi.deliveryHistory is empty")
	Page<FlexibleOrder> findAllToBeConfirmed(Pageable pageable);
	
	@Query("SELECT distinct(o) from FlexibleOrder o join o.items oi where oi.deliveryHistory is empty "
			+ "and o.customer = ?1")
	Page<FlexibleOrder> findAllToBeConfirmedByCustomer(Customer customer, Pageable pageable);
	
	
	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL) and "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP) and "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM) "
			+ "and oi.flexibleOrder.customer = ?1")
	Page<FlexibleOrder> findAllToBeShippedByCustomer(Customer customer, Pageable pageable);
	
	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL) and "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP) and "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM)")
	Page<FlexibleOrder> findAllToBeShipped(Pageable pageable);
	
	
	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL) and "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID) and "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP) "
			+ "and oi.flexibleOrder.customer = ?1")
	Page<FlexibleOrder> findAllToBePaidByCustomer(Customer customer, Pageable pageable);
	
	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL) and "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID) and "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP) ")
	Page<FlexibleOrder> findAllToBePaid(Pageable pageable);
	
	
	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL) or "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID) "
			+ "and oi.flexibleOrder.customer = ?1")
	Page<FlexibleOrder> findAllCompletedByCustomer(Customer customer, Pageable pageable);
	
	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID) or "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL)")
	Page<FlexibleOrder> findAllCompleted(Pageable pageable);
	

}
