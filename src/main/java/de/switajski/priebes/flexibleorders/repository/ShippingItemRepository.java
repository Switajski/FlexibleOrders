package de.switajski.priebes.flexibleorders.repository;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.ShippingItem;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = ShippingItem.class)
public interface ShippingItemRepository {

	List<ShippingItem> findByOrderNumber(Long orderNumber);
}
