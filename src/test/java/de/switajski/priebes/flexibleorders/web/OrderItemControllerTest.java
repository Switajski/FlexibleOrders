package de.switajski.priebes.flexibleorders.web;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashSet;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.HandlingEventService;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class OrderItemControllerTest {
	

	private ItemRepository itemRepo;
	private CustomerService customerService;
	private HandlingEventService heService;
	private CatalogProductRepository productRepository;
	
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
	
	public static final String CREATE_ORDERITEM_REQUEST_JSON2 = "{"
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
	public void shouldDeserializeOrderItem() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		Item oi = mapper.readValue(CREATE_ORDERITEM_REQUEST_JSON, 
				Item.class);
		assertTrue(oi.getProductNumber().equals(29026l));

	}

	@Transactional
	@Test
	public void shouldDeserializeOrderItems() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		Item[] ois = mapper.readValue(CREATE_ORDERITEMS_REQUEST_JSON, 
				Item[].class);
		HashSet<Long> productNumbers = new HashSet<Long>();
		for (Item oi:ois)
			productNumbers.add(oi.getProductNumber());

		assertTrue(productNumbers.contains(10055l));
		assertTrue(productNumbers.contains(44210l));
	}
	
	@Transactional
	@Test
	public void shouldDeserializeOrderItemsByJsonController() throws Exception{
		JsonController oic = new JsonController(itemRepo, customerService, heService, productRepository);
		oic.parseJsonArray(CREATE_ORDERITEMS_REQUEST_JSON);
	}

	@Test
	@Transactional
	public void shouldSaveDeserializedOrderItem() throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();

		Item oi = mapper.readValue(CREATE_ORDERITEM_REQUEST_JSON, 
				Item.class);
		assertTrue(oi.getProductNumber().equals(29026l));
		
		oi.setProduct(productRepository.findByProductNumber(oi.getProductNumber()));
		
		try {
			itemRepo.saveAndFlush(oi);
		} catch(Exception e){
			e.printStackTrace();
		}
		

	}
	
	
	public static final String FILTER_REQUEST_URL_DECODED = "http://localhost:8080/FlexibleOrders/orderitems/json?_dc=1375176055721&filter=[{\"type\":\"string\",\"value\":\"45\",\"field\":\"orderNumber\"}]&page=1&start=0&limit=25";
	public static final String FILTER_REQUEST_DECODED = "[{\"type\":\"string\",\"value\":\"45\",\"field\":\"orderNumber\"}]";
	public static final String FILTER_REQUEST_URL_ENCODED = "http://localhost:8080/FlexibleOrders/orderitems/json?_dc=1375176055721&filter=%5B%7B%22type%22%3A%22string%22%2C%22value%22%3A%2245%22%2C%22field%22%3A%22orderNumber%22%7D%5D&page=1&start=0&limit=25";

	@Test
	public void shouldDeserializeFilter() throws JsonParseException, JsonMappingException, IOException{
		JsonFilter[] typedArray = (JsonFilter[]) Array.newInstance(JsonFilter.class,1);
		
		ObjectMapper mapper = new ObjectMapper();  
	    JsonFilter[] filters = mapper.readValue(FILTER_REQUEST_DECODED, typedArray.getClass());   
	    
		JsonFilter filter = filters[0];
	}
	
}
