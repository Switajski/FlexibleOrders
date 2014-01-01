package de.switajski.priebes.flexibleorders.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;

@Repository
@RooJpaRepository(domainType = Item.class)
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
	List<Item> findByOrderNumber(Long orderNumber);
	Page<Item> findByOrderNumber(Long orderNumber, Pageable pageable);
	
	List<Item> findByCustomer(Customer customer);
	Page<Item> findByCustomer(Customer customer, Pageable pageable);
	
}
