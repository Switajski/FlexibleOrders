package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.springframework.stereotype.Component;

/**
 * Used to deserialize Java.util.Date, which is not a common JSON type, so we
 * have to create a custom serialize method;.
 */
@Component
public class JsonDateDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser jsonparser,
			DeserializationContext deserializationcontext) throws IOException,
			JsonProcessingException {

		SimpleDateFormat format = JsonFormatterConstants.DATE_FORMAT;
		String date = jsonparser.getText();
		if (StringUtils.isEmpty(date))
			return null;
		try {
			return format.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

	}
}
