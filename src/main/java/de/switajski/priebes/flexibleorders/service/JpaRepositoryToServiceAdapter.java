package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class JpaRepositoryToServiceAdapter<T> implements CrudServiceAdapter<T>{
	
	JpaRepository<T, Long> jpaRepository;
	
	
	public JpaRepositoryToServiceAdapter(JpaRepository<T, Long> jpaRepository) {
		this.jpaRepository = jpaRepository;
	}
	
	public long countAll() {
		return jpaRepository.count();
	}
	
	@Override
	public T find(Long id) {
		return (T) jpaRepository.findOne(id);
	}
	
	@Override
	public Page<T> findAll(Pageable pageable) {
		return (Page<T>) jpaRepository.findAll(pageable);
	}
	
	@Override
	public List<T> findAll() {
		return jpaRepository.findAll();
	}
	
	@Override
	public void save(T t) {
		jpaRepository.save(t);
	}
	
	@Override
	public void update(T t) {
	//TODO: Testen!
		jpaRepository.save(t);
	}
	
	@Override
	public void delete(T t) {
		jpaRepository.delete(t);
		
	}
}
