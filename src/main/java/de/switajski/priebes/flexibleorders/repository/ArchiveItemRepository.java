package de.switajski.priebes.flexibleorders.repository;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooJpaRepository(domainType = ArchiveItem.class)
public interface ArchiveItemRepository 
extends JpaRepository<ArchiveItem, Long>, 
	JpaSpecificationExecutor<ArchiveItem>, 
	ItemRepository<ArchiveItem> {
	
	
}
