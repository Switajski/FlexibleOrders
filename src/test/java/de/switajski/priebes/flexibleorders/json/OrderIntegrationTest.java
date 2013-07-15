package de.switajski.priebes.flexibleorders.json;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.OrderItemService;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class OrderIntegrationTest {

	@Autowired
	OrderItemService orderItemService;
	
	@Test
	public void shouldGetPageableOrders(){
		Page<String> pages = orderItemService.findByOrderNumberGrouped(new PageRequest(0,20));
		assertEquals(pages.getSize(), 20);
	}
	
	@Test
	public void shouldCountOrders(){
		Long ordersSize = orderItemService.countAllOrders();
		assertTrue(ordersSize != 0);
	}
}
