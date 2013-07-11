package de.switajski.priebes.flexibleorders.repository;
import de.switajski.priebes.flexibleorders.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooJpaRepository(domainType = Product.class)
public interface ProductRepository extends JpaSpecificationExecutor<Product>, JpaRepository<Product, Long> {
}
