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
	
	//TODO: replace by "ReportItemEmptySpecification"
	static String fromOrderWhereReportItemsIsEmpty = "from Order o join o.items oi where oi.reportItems is empty ";
	
	static String groupBy = " group by o";
	
	@Query("SELECT o " + fromOrderWhereReportItemsIsEmpty + groupBy)
	Page<Order> findAllToBeConfirmed(Pageable pageable);
	
	@Query("SELECT o " + fromOrderWhereReportItemsIsEmpty
			+ "and o.customer = ?1" + groupBy)
	Page<Order> findAllToBeConfirmedByCustomer(Customer customer, Pageable pageable);
	
}
