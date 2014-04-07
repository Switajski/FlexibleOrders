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
	
	@Query("SELECT distinct(o) from Order o join o.items oi where oi.deliveryHistory is empty")
	Page<Order> findAllToBeConfirmed(Pageable pageable);
	
	@Query("SELECT distinct(o) from Order o join o.items oi where oi.deliveryHistory is empty "
			+ "and o.customer = ?1")
	Page<Order> findAllToBeConfirmedByCustomer(Customer customer, Pageable pageable);
	
	
//	@Query("SELECT distinct(oi.customerOrder) from OrderItem oi where "
//			+ "oi NOT IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.CANCEL and he.orderItem = oi) and "
//			+ "oi IN "
//			+ "(SELECT he.orderItem from ReportItem he where he.orderItem = oi and "
//				+ "(SELECT sum(confirmEvent.quantity) from ReportItem confirmEvent where confirmEvent.orderItem = oi and confirmEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.CONFIRM) "
//				+ " > "
//				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from ReportItem shipEvent where shipEvent.orderItem = oi and shipEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.SHIP)"
//			+ ")"
//			+ "and oi.customerOrder.customer = ?1")
//	Page<Order> findAllToBeShippedByCustomer(Customer customer, Pageable pageable);
//	
//	@Query("SELECT distinct(oi.customerOrder) from OrderItem oi where "
//			+ "oi NOT IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.CANCEL and he.orderItem = oi) and "
//			+ "oi IN "
//			+ "(SELECT he.orderItem from ReportItem he where he.orderItem = oi and "
//				+ "(SELECT sum(confirmEvent.quantity) from ReportItem confirmEvent where confirmEvent.orderItem = oi and confirmEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.CONFIRM) "
//				+ " > "
//				+ "(SELECT coalesce(sum(shipEvent.quantity),0) from ReportItem shipEvent where shipEvent.orderItem = oi and shipEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.SHIP)"
//			+ ")")
//	Page<Order> findAllToBeShipped(Pageable pageable);
//	
//	
//	@Query("SELECT distinct(oi.customerOrder) from OrderItem oi where "
//			+ "oi NOT IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.CANCEL and he.orderItem = oi) and "
//			+ "oi NOT IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.PAID and he.orderItem = oi) and "
//			+ "oi IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.SHIP and he.orderItem = oi) "
//			+ "and oi.customerOrder.customer = ?1")
//	Page<Order> findAllToBePaidByCustomer(Customer customer, Pageable pageable);
//	
//	@Query("SELECT distinct(oi.customerOrder) from OrderItem oi where "
//			+ "oi NOT IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.CANCEL and he.orderItem = oi) and "
//			+ "oi NOT IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.PAID and he.orderItem = oi) and "
//			+ "oi IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.SHIP and he.orderItem = oi) ")
//	Page<Order> findAllToBePaid(Pageable pageable);
//	
//	
//	@Query("SELECT distinct(oi.customerOrder) from OrderItem oi where "
//			+ "oi IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.CANCEL and he.orderItem = oi) or "
//			+ "oi IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.PAID and he.orderItem = oi) "
//			+ "and oi.customerOrder.customer = ?1")
//	Page<Order> findAllCompletedByCustomer(Customer customer, Pageable pageable);
//	
//	@Query("SELECT distinct(oi.customerOrder) from OrderItem oi where "
//			+ "oi IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.PAID and he.orderItem = oi) or "
//			+ "oi IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.CANCEL and he.orderItem = oi)")
//	Page<Order> findAllCompleted(Pageable pageable);
//
//	@Query("SELECT distinct(oi.customerOrder) from OrderItem oi where "
//			+ "oi NOT IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.CANCEL and he.orderItem = oi) and "
//			+ "oi IN "
//			+ "(SELECT he.orderItem from ReportItem he where he.orderItem = oi and "
//				+ "(SELECT sum(shipEvent.quantity) from ReportItem shipEvent where shipEvent.orderItem = oi and shipEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.SHIP) "
//				+ " > "
//				+ "(SELECT coalesce(sum(invoiceEvent.quantity),0) from ReportItem invoiceEvent where invoiceEvent.orderItem = oi and invoiceEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.INVOICE)"
//			+ ")")
//	Page<Order> findAllToBeInvoiced(Pageable pageable);
//	
//	@Query("SELECT distinct(oi.customerOrder) from OrderItem oi where "
//			+ "oi NOT IN (SELECT he.orderItem from ReportItem he where he.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.CANCEL and he.orderItem = oi) and "
//			+ "oi IN "
//			+ "(SELECT he.orderItem from ReportItem he where he.orderItem = oi and "
//				+ "(SELECT sum(shipEvent.quantity) from ReportItem shipEvent where shipEvent.orderItem = oi and shipEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.SHIP) "
//				+ " > "
//				+ "(SELECT coalesce(sum(invoiceEvent.quantity),0) from ReportItem invoiceEvent where invoiceEvent.orderItem = oi and invoiceEvent.type = de.switajski.priebes.flexibleorders.domain.ReportItemType.INVOICE)"
//			+ ") "
//			+ "and oi.customerOrder.customer = ?1")
//	Page<Order> findAllToBeInvoicedByCustomer(Customer customer, Pageable pageable);
	

}
