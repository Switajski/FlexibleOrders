package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;


public interface ArchiveItemService extends CrudServiceAdapter<ArchiveItem> {
	List<ArchiveItem> findByOrderNumber(Long orderNumber);
	Page<ArchiveItem> findByOrderNumber(Long orderNumber, Pageable pageable);
}
