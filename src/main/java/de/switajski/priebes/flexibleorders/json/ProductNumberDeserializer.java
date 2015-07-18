package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import de.switajski.priebes.flexibleorders.domain.Product;

@Transactional
@Component
public class ProductNumberDeserializer extends JsonDeserializer<Product> {

	@Transactional
	@Override
	public Product deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		Product product = new Product();
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		product.setProductNumber(node.asText());
		return product;

	}

}
