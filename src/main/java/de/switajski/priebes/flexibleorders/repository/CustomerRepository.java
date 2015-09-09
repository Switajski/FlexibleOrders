package de.switajski.priebes.flexibleorders.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.switajski.priebes.flexibleorders.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
	Customer findByEmail(String email);
	Customer findByCustomerNumber(Long customerNumber);
	
	@Query("select c from Customer c where c.companyName like %?1% or c.lastName like %?1% or c.companyName like %?1%")
	Page<Customer> search(Pageable pageable, String keyword);
	
}
