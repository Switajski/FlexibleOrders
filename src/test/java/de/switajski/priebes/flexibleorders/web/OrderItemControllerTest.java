package de.switajski.priebes.flexibleorders.web;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.ProductService;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class OrderItemControllerTest {
	
	@Autowired OrderItemService orderItemService;
	@Autowired ProductService productService;
	@Autowired OrderItemRepository orderItemRepository;

	public static final String CREATE_ORDERITEM_REQUEST_JSON = ""
			+ "{\"product\":29026,"
			+ "\"orderNumber\":\"1234\","
			+ "\"invoiceNumber\":\"\","
			+ "\"orderConfirmationNumber\":\"\","
			+ "\"quantity\":5,\"priceNet\":\"\","
			+ "\"status\":\"ORDERED\","
			+ "\"expectedDelivery\":\"\""
			+ "}";

	public static final String CREATE_ORDERITEMS_REQUEST_JSON = "["
			+ "{\"product\":10055,"
			+ "\"orderNumber\":\"5678\","
			+ "\"invoiceNumber\":\"\","
			+ "\"orderConfirmationNumber\":\"\","
			+ "\"quantity\":5,\"priceNet\":null,"
			+ "\"status\":\"ORDERED\","
			+ "\"expectedDelivery\":\"\"}"
			+ ","
			+ "{\"product\":44210,"
			+ "\"orderNumber\":\"5678\","
			+ "\"invoiceNumber\":\"\","
			+ "\"orderConfirmationNumber\":\"\","
			+ "\"quantity\":4,\"priceNet\":\"\","
			+ "\"status\":\"ORDERED\","
			+ "\"expectedDelivery\":\"\""
			+ "}]";
	@Transactional
	@Test
	public void shouldDeserializeOrderItem() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		OrderItem oi = mapper.readValue(CREATE_ORDERITEM_REQUEST_JSON, 
				OrderItem.class);
		Product product = oi.getProduct();
		assertTrue(product.getProductNumber().equals(29026l));

	}

	@Transactional
	@Test
	public void shouldDeserializeOrderItems() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		OrderItem[] ois = mapper.readValue(CREATE_ORDERITEMS_REQUEST_JSON, 
				OrderItem[].class);
		HashSet<Long> productNumbers = new HashSet<Long>();
		for (OrderItem oi:ois)
			productNumbers.add(oi.getProduct().getProductNumber());

		assertTrue(productNumbers.contains(10055l));
		assertTrue(productNumbers.contains(44210l));


	}

	@Test
	@Transactional
	public void shouldSaveDeserializedOrderItem() throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();

		OrderItem oi = mapper.readValue(CREATE_ORDERITEM_REQUEST_JSON, 
				OrderItem.class);
		Product product = oi.getProduct();
		assertTrue(product.getProductNumber().equals(29026l));
		
		oi.setProduct(productService.findByProductNumber(product.getProductNumber()));
		
		try {
//			orderItemService.save(oi);
//			orderItemService.saveOrderItem(oi);
			orderItemRepository.saveAndFlush(oi);
		} catch(Exception e){
			e.printStackTrace();
		}
		

	}

}
