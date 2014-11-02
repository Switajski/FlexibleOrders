package de.switajski.priebes.flexibleorders.web;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashSet;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class JacksonDeserializationTest {
	
	public static final String CREATE_REPORTITEM_REQUEST_JSON = ""
			+ "{\"product\":1809935791,"
			+ "\"productName\":\"\","
			+ "\"customer\":2,"
			+ "\"customerName\":\"\","
			+ "\"orderNumber\":\"123\","
			+ "\"documentNumber\":\"\","
			+ "\"quantity\":4,"
			+ "\"quantityLeft\":0,"
			+ "\"priceNet\":6,"
			+ "\"status\":\"ORDERED\","
			+ "\"expectedDelivery\":\"\","
			+ "\"created\":\"\"}";
	
	public static final String CREATE_REPORTITEMS_REQUEST_JSON = "["
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

    private static final String PRODUCT_NAME = "Jasiu Wedrowiec";
	
	public static final String CREATE_REPORTITEM_REQUEST_JSON2 = "{"
			+ "\"id\":1015808,"
			+ "\"product\":10071,"
			+ "\"productName\":\"" + PRODUCT_NAME + "\","
			+ "\"customer\":2,"
			+ "\"orderNumber\":777,"
			+ "\"invoiceNumber\":null,"
			+ "\"orderConfirmationNumber\":777,"
			+ "\"quantity\":17,"
			+ "\"priceNet\":14.24,"
			+ "\"status\":\"CONFIRMED\","
			+ "\"expectedDelivery\":\""
			+ "\"}";	
	
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

	public static final String FILTER_REQUEST_URL_DECODED = "http://localhost:8080/FlexibleOrders/orderitems/json?_dc=1375176055721&filter=[{\"type\":\"string\",\"value\":\"45\",\"field\":\"orderNumber\"}]&page=1&start=0&limit=25";
	public static final String FILTER_REQUEST_DECODED = "[{\"type\":\"string\",\"value\":\"45\",\"field\":\"orderNumber\"}]";
	public static final String FILTER_REQUEST_URL_ENCODED = "http://localhost:8080/FlexibleOrders/orderitems/json?_dc=1375176055721&filter=%5B%7B%22type%22%3A%22string%22%2C%22value%22%3A%2245%22%2C%22field%22%3A%22orderNumber%22%7D%5D&page=1&start=0&limit=25";
	
	@Test
	public void shouldDeserializeReportItem() throws JsonParseException, JsonMappingException, IOException {
	    // GIVEN
		ObjectMapper mapper = new ObjectMapper();
		
		// WHEN
		ItemDto reportItem = mapper.readValue(CREATE_REPORTITEM_REQUEST_JSON, 
				ItemDto.class);
		
		//THEN
		assertThat(reportItem.product, is(equalTo("1809935791")));
	}
	
	@Test
    public void readValue_shouldDeserializeEmptyStringsAsNull() throws JsonParseException, JsonMappingException, IOException {
        // GIVEN
	    ObjectMapper mapper = new ObjectMapper();
        mapper.getSerializationConfig();
        
        // WHEN
        ItemDto reportItem = mapper.readValue(CREATE_REPORTITEM_REQUEST_JSON, 
                ItemDto.class);
        
        // THEN
        assertThat(reportItem.productName, is(nullValue()));
    }
	
	@Test
    public void readValue_shouldDeserializeStrings() throws JsonParseException, JsonMappingException, IOException {
        // GIVEN
        ObjectMapper mapper = new ObjectMapper();
        mapper.getSerializationConfig();
        
        // WHEN
        ItemDto reportItem = mapper.readValue(CREATE_REPORTITEM_REQUEST_JSON2, 
                ItemDto.class);
        
        // THEN
        assertThat(reportItem.productName, is(equalTo(PRODUCT_NAME)));
    }

	@Test
	public void shouldDeserializeReporItems() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		ItemDto[] ois = mapper.readValue(CREATE_REPORTITEMS_REQUEST_JSON, 
				ItemDto[].class);
		HashSet<String> productNumbers = new HashSet<String>();
		for (ItemDto oi:ois)
			productNumbers.add(oi.product);

		assertTrue(productNumbers.contains("10055"));
		assertTrue(productNumbers.contains("44210"));
	}
	
	@Test
	public void shouldDeserializeFilter() throws JsonParseException, JsonMappingException, IOException{
		JsonFilter[] typedArray = (JsonFilter[]) Array.newInstance(JsonFilter.class,1);
		
		ObjectMapper mapper = new ObjectMapper();  
	    JsonFilter[] filters = mapper.readValue(FILTER_REQUEST_DECODED, typedArray.getClass());   
	    
		JsonFilter filter = filters[0];
		
		assertNotNull(filter.getType());
		assertNotNull(filter.getValue());
	}
	
}
