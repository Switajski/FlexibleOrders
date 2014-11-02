package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
		product.setProductNumber(node.getTextValue());
		return product;

	}

}
