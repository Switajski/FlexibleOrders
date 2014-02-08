package de.switajski.priebes.flexibleorders.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;

public interface OrderRepository extends JpaRepository<FlexibleOrder, Long>, JpaSpecificationExecutor<FlexibleOrder>{

	FlexibleOrder findByOrderNumber(String orderNumber);

	Page<FlexibleOrder> findByCustomer(Customer customer, Pageable pageable);

	List<FlexibleOrder> findByOrderNumberLike(String orderNumber);

}
