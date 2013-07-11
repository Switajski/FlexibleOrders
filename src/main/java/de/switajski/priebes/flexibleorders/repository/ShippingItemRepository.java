package de.switajski.priebes.flexibleorders.repository;
import java.util.List;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooJpaRepository(domainType = ShippingItem.class)
public interface ShippingItemRepository extends JpaSpecificationExecutor<ShippingItem>, JpaRepository<ShippingItem, Long> {

	List<ShippingItem> findByOrderNumber(Long orderNumber);
}
