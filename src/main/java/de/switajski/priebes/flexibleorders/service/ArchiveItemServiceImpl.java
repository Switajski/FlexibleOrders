package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.repository.ArchiveItemRepository;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;

public class ArchiveItemServiceImpl extends JpaRepositoryToServiceAdapter<ArchiveItem> implements ArchiveItemService {

	@Autowired
	public ArchiveItemServiceImpl(ArchiveItemRepository jpaRepository) {
		super(jpaRepository);
	}
	
	@Override
	public List<ArchiveItem> findByOrderNumber(Long orderNumber) {
		return ((ItemRepository<ArchiveItem>) this.jpaRepository).findByOrderNumber(orderNumber);
	}

	@Override
	public Page<ArchiveItem> findByOrderNumber(Long orderNumber,
			Pageable pageable) {
		return ((ItemRepository<ArchiveItem>) this.jpaRepository).findByOrderNumber(orderNumber, pageable);
	}
	
	@Override
	public Page<ArchiveItem> findCompleted(Pageable pageable){
		return ((ItemRepository<ArchiveItem>) this.jpaRepository).findByStatus(Status.COMPLETED, pageable);
	}
}
