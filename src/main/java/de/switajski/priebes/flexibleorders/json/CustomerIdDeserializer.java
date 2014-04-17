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

import de.switajski.priebes.flexibleorders.domain.Customer;

@Transactional
@Component
public class CustomerIdDeserializer extends JsonDeserializer<Customer> {

	@Transactional
	@Override
	public Customer deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		Customer customer = new Customer();
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		customer.setId(node.getLongValue());
		return customer;

	}

}
