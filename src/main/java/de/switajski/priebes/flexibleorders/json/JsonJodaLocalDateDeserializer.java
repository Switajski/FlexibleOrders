package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

public class JsonJodaLocalDateDeserializer extends JsonDeserializer<LocalDate>{

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        DateTimeFormatter format = JsonFormatterConstants.DATE_TIME_FORMATTER;
        String date = jp.getText();
        if (StringUtils.isEmpty(date))
            return null;
        return format.parseDateTime(date).toLocalDate();
    }

}
