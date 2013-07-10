package de.switajski.priebes.flexibleorders.repository;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = ArchiveItem.class)
public interface ArchiveItemRepository {
	
	List<ArchiveItem> findByOrderNumber(Long orderNumber);
}
