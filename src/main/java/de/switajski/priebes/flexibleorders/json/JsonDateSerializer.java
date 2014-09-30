package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.stereotype.Component;

/**
 * Used to serialize Java.util.Date, which is not a common JSON type, so we have
 * to create a custom serialize method;.
 * 
 * @author Loiane Groner http://loianegroner.com (English) http://loiane.com
 *         (Portuguese)
 */
@Component
public class JsonDateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date date, JsonGenerator gen,
			SerializerProvider provider)
			throws IOException, JsonProcessingException {

		String formattedDate = JsonFormatterConstants.DATE_FORMAT.format(date);

		gen.writeString(formattedDate);
	}

}