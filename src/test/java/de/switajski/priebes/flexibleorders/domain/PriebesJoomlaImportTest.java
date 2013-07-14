package de.switajski.priebes.flexibleorders.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.datasources.ImporterService;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@RooIntegrationTest(entity = OrderItem.class)
public class PriebesJoomlaImportTest {
	
	@Autowired
	ImporterService pji;
	
	@Autowired CustomerRepository customerRepository;
	
	@Transactional
	@Test
	public void shouldImportCustomer(){
		pji.importCustomers();
		assertFalse(customerRepository.count()==0);
	}
	

}
