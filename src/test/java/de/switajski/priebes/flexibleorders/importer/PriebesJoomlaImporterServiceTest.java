package de.switajski.priebes.flexibleorders.importer;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.datasources.ImporterService;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ProductRepository;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@RooIntegrationTest(entity = OrderItem.class)
public class PriebesJoomlaImporterServiceTest {
	
	@Autowired
	ImporterService pji;
	
	@Autowired CustomerRepository customerRepository;
	@Autowired ProductRepository productRepository;
	
	@Rollback(false)
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
	}
	
	@Rollback(false)
	@Transactional
	@Test
	public void shouldImportOrderItems(){
		pji.importCustomers();
		pji.importProducts();
		pji.importOrderItems();
		assertFalse("Product repository is empty!", orderItemRepository.count()==0);
	}
	
}
