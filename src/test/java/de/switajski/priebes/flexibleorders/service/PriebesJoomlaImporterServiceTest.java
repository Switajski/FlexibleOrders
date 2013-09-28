package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ProductRepository;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class PriebesJoomlaImporterServiceTest {
	
	@Autowired
	ImporterService pji;
	
	@Autowired CustomerRepository customerRepository;
	@Autowired ProductRepository productRepository;
	@Autowired OrderItemRepository orderItemRepository;
	
	/*@Rollback(false)
	@Transactional
	@Test
	public void shouldImportCustomer(){
		pji.importCustomers();
		assertFalse(customerRepository.count()==0);
	}
	
	@Rollback(false)
	@Transactional
	@Test
	public void shouldImportProducts(){
		pji.importProducts();
		assertFalse("Product repository is empty!", productRepository.count()==0);
	}*/
	
	@Rollback(false)
	@Transactional
	@Test
	public void shouldImportOrderItems(){
		pji.importCustomers();
		pji.importProducts();
		pji.importPrices();
		pji.importOrderItems();
		assertFalse("Product repository is empty!", orderItemRepository.count()==0);
	}
	
}
