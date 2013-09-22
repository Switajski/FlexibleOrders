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

	ArchiveItemRepository air;
	
	@Autowired
	public ArchiveItemServiceImpl(ArchiveItemRepository jpaRepository) {
		super(jpaRepository);
		this.air = jpaRepository;
	}
	
	@Override
	public List<ArchiveItem> findByOrderNumber(Long orderNumber) {
		return air.findByOrderNumber(orderNumber);
	}

	@Override
	public Page<ArchiveItem> findByOrderNumber(Long orderNumber,
			Pageable pageable) {
		return air.findByOrderNumber(orderNumber, pageable);
	}
	
	@Override
	public Page<ArchiveItem> findOpen(Pageable pageable){
		return air.findOpen( pageable);
	}

	@Override
	public Page<ArchiveItem> findByAccountNumber(Long accountNumber,
			Pageable pageable) {
		air.findByAccountNumber(accountNumber, pageable);
		return null;
	}
}
