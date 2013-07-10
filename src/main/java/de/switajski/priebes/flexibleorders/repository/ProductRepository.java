package de.switajski.priebes.flexibleorders.repository;
import de.switajski.priebes.flexibleorders.domain.Product;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Product.class)
public interface ProductRepository {
}
