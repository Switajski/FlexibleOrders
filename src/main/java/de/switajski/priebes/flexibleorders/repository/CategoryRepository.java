package de.switajski.priebes.flexibleorders.repository;
import de.switajski.priebes.flexibleorders.domain.Category;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Category.class)
public interface CategoryRepository {
}
