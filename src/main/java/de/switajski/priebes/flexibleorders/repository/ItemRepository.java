package de.switajski.priebes.flexibleorders.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.switajski.priebes.flexibleorders.domain.Item;

public interface ItemRepository<T extends Item> {
	List<T> findByOrderNumber(Long orderNumber);
	Page<T> findByOrderNumber(Long orderNumber, Pageable pageable);
}
