package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReadService<T> {
	 long countAll();
	 T find(Long id);
	 Page<T> findAll(Pageable pageable);
	 List<T> findAll();
}
