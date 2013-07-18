package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudServiceAdapter<T> {
	 long countAll();
	 T find(Long id);
	 Page<T> findAll(Pageable pageable);
	 List<T> findAll();
	 void save(T t);
	 void delete(T t);
	 void update(T t);
}
