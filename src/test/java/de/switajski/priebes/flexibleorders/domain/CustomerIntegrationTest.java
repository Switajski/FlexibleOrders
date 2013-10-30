package de.switajski.priebes.flexibleorders.domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import de.switajski.priebes.flexibleorders.integrationtest.AbstractIntegrationTest;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CustomerBuilder;

public class CustomerIntegrationTest extends AbstractIntegrationTest<Customer>{

	@Autowired private CustomerRepository customerRepository;
	
	@Override
	protected Customer createEntity() {
		return new CustomerBuilder("street", "city", 1234, "email@nowhere.com").build();
	}

	@Override
	protected JpaRepository<Customer, ?> getRepository() {
		return customerRepository;
	}
}
