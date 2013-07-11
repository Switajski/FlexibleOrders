package de.switajski.priebes.flexibleorders.repository;
import java.util.List;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooJpaRepository(domainType = OrderItem.class)
public interface OrderItemRepository extends JpaSpecificationExecutor<OrderItem>, JpaRepository<OrderItem, Long> {
	
	List<OrderItem> findByOrderNumber(Long orderNumber);
}
