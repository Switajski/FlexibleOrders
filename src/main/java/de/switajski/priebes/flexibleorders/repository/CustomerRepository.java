package de.switajski.priebes.flexibleorders.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.Customer;

@Repository
@RooJpaRepository(domainType = Customer.class)
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
	Customer findByEmail(String email);
}
