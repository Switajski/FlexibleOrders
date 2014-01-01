package de.switajski.priebes.flexibleorders.domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import de.switajski.priebes.flexibleorders.domain.specification.Address;
import de.switajski.priebes.flexibleorders.integrationtest.AbstractIntegrationTest;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CustomerBuilder;

public class CustomerIntegrationTest extends AbstractIntegrationTest<Customer>{

	@Autowired private CustomerRepository customerRepository;
	
	@Override
	protected Customer createEntity() {
		return new CustomerBuilder(
				"shortName",
				"email@nowhere.com",
				new Address(
						"name1", "name2", 
						"street",
						1234, "city",
						Country.GERMANY)
						).build();
	}

	@Override
	protected JpaRepository<Customer, Long> getRepository() {
		return customerRepository;
	}
}
