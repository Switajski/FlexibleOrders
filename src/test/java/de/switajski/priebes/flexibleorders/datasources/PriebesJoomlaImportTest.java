package de.switajski.priebes.flexibleorders.datasources;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.repository.CustomerRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
public class PriebesJoomlaImportTest {
	
	PriebesJoomlaImport pji;
	
	@Autowired CustomerRepository customerRepository;
	
	@Before
	public void shouldInitialize(){
		pji = new PriebesJoomlaImport();
	}
	
	@Transactional
	@Test
	public void shouldImportCustomer(){
		assertFalse(customerRepository.count()==0);
	}

}
