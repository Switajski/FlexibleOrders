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
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL and he.orderItem = oi) and "
			+ "oi IN "
			+ "(SELECT he.orderItem from HandlingEvent he where he.orderItem = oi and "
				+ "(SELECT sum(confirmEvent.quantity) from HandlingEvent confirmEvent where confirmEvent.orderItem = oi and confirmEvent.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM) "
				+ " > "
				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from HandlingEvent shipEvent where shipEvent.orderItem = oi and shipEvent.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP)"
			+ ")"
			+ "and oi.flexibleOrder.customer = ?1")
	Page<FlexibleOrder> findAllToBeShippedByCustomer(Customer customer, Pageable pageable);
	
	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL and he.orderItem = oi) and "
			+ "oi IN "
			+ "(SELECT he.orderItem from HandlingEvent he where he.orderItem = oi and "
				+ "(SELECT sum(confirmEvent.quantity) from HandlingEvent confirmEvent where confirmEvent.orderItem = oi and confirmEvent.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM) "
				+ " > "
				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from HandlingEvent shipEvent where shipEvent.orderItem = oi and shipEvent.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP)"
			+ ")")
	Page<FlexibleOrder> findAllToBeShipped(Pageable pageable);
	
	
	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL and he.orderItem = oi) and "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID and he.orderItem = oi) and "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP and he.orderItem = oi) "
			+ "and oi.flexibleOrder.customer = ?1")
	Page<FlexibleOrder> findAllToBePaidByCustomer(Customer customer, Pageable pageable);
	
	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL and he.orderItem = oi) and "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID and he.orderItem = oi) and "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP and he.orderItem = oi) ")
	Page<FlexibleOrder> findAllToBePaid(Pageable pageable);
	
	
	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL and he.orderItem = oi) or "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID and he.orderItem = oi) "
			+ "and oi.flexibleOrder.customer = ?1")
	Page<FlexibleOrder> findAllCompletedByCustomer(Customer customer, Pageable pageable);
	
	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID and he.orderItem = oi) or "
			+ "oi IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL and he.orderItem = oi)")
	Page<FlexibleOrder> findAllCompleted(Pageable pageable);

	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL and he.orderItem = oi) and "
			+ "oi IN "
			+ "(SELECT he.orderItem from HandlingEvent he where he.orderItem = oi and "
				+ "(SELECT sum(shipEvent.quantity) from HandlingEvent shipEvent where shipEvent.orderItem = oi and shipEvent.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP) "
				+ " > "
				+ "(SELECT coalesce(sum(invoiceEvent.quantity),0) from HandlingEvent invoiceEvent where invoiceEvent.orderItem = oi and invoiceEvent.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.INVOICE)"
			+ ")")
	Page<FlexibleOrder> findAllToBeInvoiced(Pageable pageable);
	
	@Query("SELECT distinct(oi.flexibleOrder) from OrderItem oi where "
			+ "oi NOT IN (SELECT he.orderItem from HandlingEvent he where he.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.CANCEL and he.orderItem = oi) and "
			+ "oi IN "
			+ "(SELECT he.orderItem from HandlingEvent he where he.orderItem = oi and "
				+ "(SELECT sum(shipEvent.quantity) from HandlingEvent shipEvent where shipEvent.orderItem = oi and shipEvent.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP) "
				+ " > "
				+ "(SELECT coalesce(sum(invoiceEvent.quantity),0) from HandlingEvent invoiceEvent where invoiceEvent.orderItem = oi and invoiceEvent.type = de.switajski.priebes.flexibleorders.domain.HandlingEventType.INVOICE)"
			+ ") "
			+ "and oi.flexibleOrder.customer = ?1")
	Page<FlexibleOrder> findAllToBeInvoicedByCustomer(Customer customer, Pageable pageable);
	

}
