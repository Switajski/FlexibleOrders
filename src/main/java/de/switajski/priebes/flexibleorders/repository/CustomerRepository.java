package de.switajski.priebes.flexibleorders.repository;
import de.switajski.priebes.flexibleorders.domain.Customer;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Customer.class)
public interface CustomerRepository {
}
