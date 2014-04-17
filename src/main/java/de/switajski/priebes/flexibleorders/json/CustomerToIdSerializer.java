package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import de.switajski.priebes.flexibleorders.domain.Customer;

public class CustomerToIdSerializer extends JsonSerializer<Customer> {

	@Override
	public void serialize(Customer value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeNumber(value.getId());

	}

}
