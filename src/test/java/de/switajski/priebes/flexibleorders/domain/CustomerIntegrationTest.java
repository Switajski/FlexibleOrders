package de.switajski.priebes.flexibleorders.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.testhelper.AbstractIntegrationTest;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;

public class CustomerIntegrationTest extends AbstractIntegrationTest<Customer> {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	protected Customer createEntity() {
		Long custNo = 15234L;

		return createCustomer(custNo);
	}

    private Customer createCustomer(Long custNo) {
        return new CustomerBuilder()
        .edward()
        .setCustomerNumber(custNo)
        .build();
    }

	@Override
	protected JpaRepository<Customer, Long> getRepository() {
		return customerRepository;
	}
	
	@Test
	public void searchShouldFindCustomerByKeyword(){
	    // GIVEN
        customerRepository.save(new CustomerBuilder().edward().build());
        customerRepository.save(new CustomerBuilder().yvonne().build());
	    
	    // WHEN
	    Page<Customer> customers = customerRepository.search(
	            new PageRequest(0,10), "urn");
	    // THEN
	    assertThat(customers.getContent().size(), is(1));
	}
}
