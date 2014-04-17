package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.assertFalse;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class PriebesJoomlaImporterServiceTest {

	@Autowired
	ImporterService pji;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	CatalogProductRepository productRepository;
	@Autowired
	OrderItemRepository itemRepository;

	@Rollback(false)
	@Ignore
	@Test
	public void shouldImportOrderItems() {
		// pji.importCustomers();
		// customerRepository.flush();
		// assertFalse("OrderItemRepository repository is empty!",
		// customerRepository.count()==0);

		pji.importProducts();
		pji.importPrices();
		assertFalse(
				"OrderItemRepository repository is empty!",
				productRepository.count() == 0);

		// pji.importOrderItems();
		// assertFalse("OrderItemRepository repository is empty!",
		// itemRepository.count()==0);
	}

}
