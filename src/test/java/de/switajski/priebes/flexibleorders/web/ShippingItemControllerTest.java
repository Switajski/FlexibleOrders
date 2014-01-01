package de.switajski.priebes.flexibleorders.web;

import static org.junit.Assert.assertTrue;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Item;


@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class ShippingItemControllerTest {

	public static final String CREATE_SHIPPING_ITEM_REQUEST_JSON = "{"
			+ "\"id\":1015808,"
			+ "\"product\":10071,"
			+ "\"customer\":2,"
			+ "\"orderNumber\":777,"
			+ "\"invoiceNumber\":null,"
			+ "\"orderConfirmationNumber\":777,"
			+ "\"quantity\":17,"
			+ "\"priceNet\":14.24,"
			+ "\"status\":\"CONFIRMED\","
			+ "\"expectedDelivery\":\""
			+ "\"}";	
	
	@Transactional
	@Test
	public void shouldDeserializeShippingItem() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		Item oi = mapper.readValue(CREATE_SHIPPING_ITEM_REQUEST_JSON, 
				Item.class);
		assertTrue(oi.getProductNumber().equals(10071l));

	}
	
}
