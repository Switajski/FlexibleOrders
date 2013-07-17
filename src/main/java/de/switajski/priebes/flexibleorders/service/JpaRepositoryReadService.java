package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class JpaRepositoryReadService<T> implements EntityReadService<T>{
	
	@SuppressWarnings("rawtypes")
	JpaRepository jpaRepository;
	
	
	public JpaRepositoryReadService(@SuppressWarnings("rawtypes") JpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}
	
	public long countAll() {
		return jpaRepository.count();
	}
	
	@SuppressWarnings("unchecked")
	public T find(Long id) {
		return (T) jpaRepository.findOne(id);
	}
	
	@SuppressWarnings("unchecked")
	public Page<T> findAll(Pageable pageable) {
		return (Page<T>) jpaRepository.findAll(pageable);
	}
		
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return jpaRepository.findAll();
	}
}
