package de.switajski.priebes.flexibleorders.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.testhelper.AbstractIntegrationTest;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;

public class CustomerIntegrationTest extends AbstractIntegrationTest<Customer> {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	protected Customer createEntity() {
		Long custNo = 15234L;

		return new CustomerBuilder(
				custNo,
				"email" + custNo + "nowhere.com",
				new Address(
						"name1", "name2",
						"street",
						1234, "city",
						Country.DEUTSCHLAND)).build();
	}

	@Override
	protected JpaRepository<Customer, Long> getRepository() {
		return customerRepository;
	}
}
