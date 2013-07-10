package de.switajski.priebes.flexibleorders.repository;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = OrderItem.class)
public interface OrderItemRepository {
}
