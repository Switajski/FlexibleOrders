package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.LocalDate;

public class JsonJodaLocalDateSerializer extends JsonSerializer<LocalDate>{

    @Override
    public void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(value.toString(JsonFormatterConstants.DATE_TIME_FORMATTER));
    }

}
