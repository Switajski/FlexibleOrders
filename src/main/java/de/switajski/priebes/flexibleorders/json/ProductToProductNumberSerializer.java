package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import de.switajski.priebes.flexibleorders.domain.Product;

public class ProductToProductNumberSerializer extends JsonSerializer<Product> {

	@Override
	public void serialize(Product value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeNumber(value.getProductNumber());
		
	}

}
