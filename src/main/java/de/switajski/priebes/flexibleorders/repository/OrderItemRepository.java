package de.switajski.priebes.flexibleorders.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Repository
@RooJpaRepository(domainType = OrderItem.class)
public interface OrderItemRepository extends JpaSpecificationExecutor<OrderItem>, JpaRepository<OrderItem, Long> {
	
	List<OrderItem> findByOrderNumber(long orderNumber);

	Page<OrderItem> findByOrderNumber(long orderNumber, Pageable pageable);

}
