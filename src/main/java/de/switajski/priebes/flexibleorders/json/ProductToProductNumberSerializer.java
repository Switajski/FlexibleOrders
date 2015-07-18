package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.switajski.priebes.flexibleorders.domain.Product;

public class ProductToProductNumberSerializer extends JsonSerializer<Product> {

	@Override
	public void serialize(Product value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeNumber(value.getProductNumber());
		
	}

}
