package de.switajski.priebes.flexibleorders.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;

@Repository
@RooJpaRepository(domainType = ArchiveItem.class)
public interface ArchiveItemRepository 
extends JpaRepository<ArchiveItem, Long>, 
	JpaSpecificationExecutor<ArchiveItem>, 
	ItemRepository<ArchiveItem> {
	
	@Query("select i "
			+ "from ArchiveItem i "
			+ "where i.quantityLeft != 0 ")
	Page<ArchiveItem> findOpen(Pageable pageable);

	@Query("select i "
			+ "from ArchiveItem i "
			+ "where i.quantityLeft != 0 "
			+ "and i.customer = ?1 "
			+ "order by max(i.created)")
	Page<ArchiveItem> findByCustomerAndCompleted(Customer customer,
			Pageable pageable);

	@Query("select i "
			+ "from ArchiveItem i "
			+ "where i.quantityLeft != 0 "
			+ "and i.customer = ?1 "
			+ "order by i.created")
	List<ArchiveItem> findByCustomerAndOpen(Customer customer);
	
	
}
