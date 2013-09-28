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
import de.switajski.priebes.flexibleorders.domain.Product;

@Repository
@RooJpaRepository(domainType = OrderItem.class)
public interface OrderItemRepository 
extends JpaSpecificationExecutor<OrderItem>, 
	JpaRepository<OrderItem, Long>, 
	ItemRepository<OrderItem>{
	
	/**
	 * 
	 * @param pageable
	 * @return
	 */
	@Query("select oi.orderNumber "
			+ "from OrderItem oi "
			+ "where oi.customer = ?1 "
			+ "group by oi.orderNumber "
			+ "order by max(oi.created)")
	Page<Long> getAllOrderNumbers(Customer customer, Pageable pageable);
	
	@Query("select oi.orderNumber "
			+ "from OrderItem oi "
			+ "group by oi.orderNumber "
			+ "order by max(oi.created)")
	Page<Long> getAllOrderNumbers(Pageable pageable);
	
	@Query("select oi.orderNumber "
			+ "from OrderItem oi "
			+ "group by oi.orderNumber "
			+ "order by max(oi.created)")
	List<Long> getAllOrderNumbers();
	
	@Query("select count(DISTINCT oi.orderNumber) "
			+ "from OrderItem oi")
	Long countAllOrders();

	@Query("select oi.orderNumber "
			+ "from OrderItem oi "
			+ "where oi.customer = ?1 "
			+ "group by oi.orderNumber "
			+ "order by max(oi.created)")
	List<Long> getAllOrderNumbers(Customer customer);
	
	List<OrderItem> findByOrderNumberAndProduct(Long orderNumber, Product product);

	@Query("select i "
			+ "from OrderItem i "
			+ "where i.quantityLeft != 0 ")
	Page<OrderItem> findOpen(Pageable pageable);
	
	@Query("select i "
			+ "from OrderItem i "
			+ "where i.quantityLeft != 0 "
			+ "order by i.created")
	List<OrderItem> findOpen();
	
	@Query("select i "
			+ "from OrderItem i "
			+ "where i.quantityLeft != 0 "
			+ "and i.customer = ?1 ")
	Page<OrderItem> findByCustomerAndOpen(Customer customer,
			Pageable pageable);
	
	@Query("select i "
			+ "from OrderItem i "
			+ "where i.quantityLeft != 0 "
			+ "and i.customer = ?1 "
			+ "order by i.created")
	List<OrderItem> findByCustomerAndOpen(Customer customer);

}
