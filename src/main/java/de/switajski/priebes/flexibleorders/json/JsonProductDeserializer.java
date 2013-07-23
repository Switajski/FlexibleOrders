package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.service.ProductService;

@Transactional
@Component
public class JsonProductDeserializer extends JsonDeserializer<Product> {

	ProductService productService;
	
	@Autowired
	public void setProductService(ProductService productService){
		this.productService = productService;
	}
	
	@Transactional
	@Override
	public Product deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		System.out.println("asdfasdfasdfasdf");
		System.out.println(productService.findByProductNumber(jp.getLongValue()));
		return productService.findByProductNumber(jp.getLongValue());
	}
	
	
}
