package de.switajski.priebes.flexibleorders.repository;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = InvoiceItem.class)
public interface InvoiceItemRepository {
	
	List<InvoiceItem> findByOrderNumber(Long orderNumber);
}
