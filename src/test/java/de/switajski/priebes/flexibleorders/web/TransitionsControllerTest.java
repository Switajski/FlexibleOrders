package de.switajski.priebes.flexibleorders.web;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.web.entities.ReportItem;

public class TransitionsControllerTest {

	private static String CREATE_ORDER_JSON_REQUEST = 
			"["
			+ "{\"product\":386706704,\"productName\":\"\",\"customer\":3,\"customerName\":\"\",\"orderNumber\":\"1358017378243\",\"invoiceNumber\":\"\",\"documentNumber\":\"\",\"quantity\":3,\"quantityLeft\":0,\"priceNet\":\"\",\"status\":\"ORDERED\",\"expectedDelivery\":\"\",\"created\":\"\"},"
			+ "{\"product\":1540693684,\"productName\":\"\",\"customer\":3,\"customerName\":\"\",\"orderNumber\":\"1358017378243\",\"invoiceNumber\":\"\",\"documentNumber\":\"\",\"quantity\":4,\"quantityLeft\":0,\"priceNet\":null,\"status\":\"ORDERED\",\"expectedDelivery\":null,\"created\":\"\"}"
			+ "]";
	
	@Transactional
	@Test
	public void shouldDeserializeCreateOrderRequest() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		ReportItem ri = mapper.readValue(CREATE_ORDER_JSON_REQUEST, 
				ReportItem.class);

	}
	
}
