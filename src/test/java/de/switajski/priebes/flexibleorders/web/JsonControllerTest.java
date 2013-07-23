package de.switajski.priebes.flexibleorders.web;

import static org.junit.Assert.*;

import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class JsonControllerTest {

	public static final String CREATE_ORDERITEM_REQUEST_JSON = "{\"data\":"
			+ "{\"product\":10055,"
			+ "\"orderNumber\":\"P.123\","
			+ "\"invoiceNumber\":\"\","
			+ "\"orderConfirmationNumber\":\"\","
			+ "\"quantity\":0,"
			+ "\"priceNet\":\"\","
			+ "\"status\":\"ORDERED\","
			+ "\"expectedDelivery\":\"\""
			+ "}"
			+ "}";
	
	@Transactional
	@Test
	public void shouldDeserializeOrderItem() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		try {
			mapper.readValue(CREATE_ORDERITEM_REQUEST_JSON, 
					new TypeReference<Map<String, OrderItem>>(){});
		}
		catch (Exception e){
			throw e;
		}
	}
		
}
