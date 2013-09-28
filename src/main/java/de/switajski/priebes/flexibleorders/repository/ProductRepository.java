package de.switajski.priebes.flexibleorders.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.Product;

@Repository
@RooJpaRepository(domainType = Product.class)
public interface ProductRepository extends JpaSpecificationExecutor<Product>, JpaRepository<Product, Long> {
	Product findByProductNumber(Long productNumber);
	Product findByName(String name);
	
}
