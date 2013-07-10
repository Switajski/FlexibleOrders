package de.switajski.priebes.flexibleorders.repository;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = OrderItem.class)
public interface OrderItemRepository {
	
	List<OrderItem> findByOrderNumber(Long orderNumber);
}
