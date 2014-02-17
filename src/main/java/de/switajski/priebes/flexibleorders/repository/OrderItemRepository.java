package de.switajski.priebes.flexibleorders.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Repository
@RooJpaRepository(domainType = OrderItem.class)
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {

	// Emergency solution until specification framework is set up
	@Query("SELECT oi from OrderItem oi where oi.deliveryHistory is empty")
	List<OrderItem> findAllOrdered();

	@Query("SELECT oi from OrderItem oi where oi.deliveryHistory is empty")
	Page<OrderItem> findAllOrdered(Pageable pageable);

	@Query("SELECT oi from OrderItem oi where oi.deliveryHistory is empty"
			+ " and oi.flexibleOrder.customer = ?1")
	Page<OrderItem> findAllOrderedByCustomer(Customer customer, Pageable pageable);


	// Emergency solution until specification framework is set up
	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM")
	List<OrderItem> findAllConfirmed();

	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM")
	Page<OrderItem> findAllConfirmed(Pageable pageable);

	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM and "
			+ "oi.flexibleOrder.customer = ?1")
	Page<OrderItem> findAllConfirmedByCustomer(Customer customer, Pageable pageable);


	// Emergency solution until specification framework is set up
	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP")
	List<OrderItem> findAllShipped();

	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP")
	Page<OrderItem> findAllShipped(Pageable pageable);

	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP and "
			+ "oi.flexibleOrder.customer = ?1")
	Page<OrderItem> findAllShippedByCustomer(Customer customer, Pageable pageable);


//TODO adjust query
	// Emergency solution until specification framework is set up
	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID")
	List<OrderItem> findAllCompleted();

	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID")
	Page<OrderItem> findAllCompleted(Pageable pageable);

	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID and "
			+ "oi.flexibleOrder.customer = ?1")
	Page<OrderItem> findAllCompletedByCustomer(Customer customer, Pageable pageable);

	// Emergency solution until specification framework is set up
	@Query("SELECT oi from OrderItem oi where oi.deliveryHistory is empty")
	Page<OrderItem> findAllToBeConfirmed(Pageable pageable);
	
	@Query("SELECT oi from OrderItem oi where oi.deliveryHistory is empty "
			+ "and oi.flexibleOrder.customer =?1")
	Page<OrderItem> findAllToBeConfirmedByCustomer(Customer customer, Pageable pageable);
	
	
	// Emergency solution until specification framework is set up
	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM")
	List<OrderItem> findAllToBeShipped();

	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM")
	Page<OrderItem> findAllToBeShipped(Pageable pageable);

	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM and "
			+ "oi.flexibleOrder.customer = ?1")
	Page<OrderItem> findAllToBeShippedByCustomer(Customer customer, Pageable pageable);

	
	
	// Emergency solution until specification framework is set up
	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM")
	List<OrderItem> findAllToBePaid();

	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM")
	Page<OrderItem> findAllToBePaid(Pageable pageable);

	@Query("SELECT oi from OrderItem oi join oi.deliveryHistory dh where dh.type = "
			+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM and "
			+ "oi.flexibleOrder.customer = ?1")
	Page<OrderItem> findAllToBePaidByCustomer(Customer customer, Pageable pageable);

	@Query("SELECT oi from OrderItem oi where oi.flexibleOrder.customer = ?1")
	Page<OrderItem> findByCustomer(Customer customer, Pageable pageable);

	@Query("SELECT oi from OrderItem oi where oi.flexibleOrder.customer = ?1")
	List<OrderItem> findByCustomer(Customer customer);

}
