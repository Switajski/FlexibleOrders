package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.repository.ArchiveItemRepository;

public class ArchiveItemServiceImpl extends JpaRepositoryToServiceAdapter<ArchiveItem> implements ArchiveItemService {

	@Autowired
	public ArchiveItemServiceImpl(ArchiveItemRepository jpaRepository) {
		super(jpaRepository);
	}
	
	
}
