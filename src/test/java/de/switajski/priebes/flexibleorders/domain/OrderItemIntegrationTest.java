package de.switajski.priebes.flexibleorders.domain;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;

@RooIntegrationTest(entity = OrderItem.class)
public class OrderItemIntegrationTest {
		
	@Autowired
	ShippingItemRepository shippingItemRepository;
	
	@Transactional
	@Test
	public void shouldConfirm(){
		boolean toSupplier = false;
		
		OrderItem orderItem = dod.getSpecificOrderItem(12);
		ShippingItem shippingItem = orderItem.confirm(toSupplier);
		shippingItemRepository.saveAndFlush(shippingItem);
		
		List<ShippingItem> sis = shippingItemRepository.findByOrderNumber(shippingItem.getOrderNumber());
		
		assertNotNull(sis);
		
	}
}
