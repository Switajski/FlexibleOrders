package de.switajski.priebes.flexibleorders.repository;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class ShippingItemRepositoryTest {

	@Autowired ShippingItemRepository shippingItemRepository;
	@Autowired CustomerRepository customerRepository;
	
	@Test
	public void shouldFindOpen(){
		List<ShippingItem> ois = shippingItemRepository.findOpen();
		assertFalse(ois.isEmpty());
	}
	
	@Test
	public void shouldFindOpenPageable(){
		Page<ShippingItem> page = shippingItemRepository.findOpen(new PageRequest(1, 10));
		assertFalse(page.getTotalElements() == 0l);
	}
	
	@Test
	public void shouldFindByCustomerAndOpen(){
		
		Customer c = customerRepository.findOne(1l);
		Page<ShippingItem> page = shippingItemRepository.findByCustomerAndOpen(c, new PageRequest(1, 10));
		assertFalse(page.getTotalElements() == 0l);
	}

}
