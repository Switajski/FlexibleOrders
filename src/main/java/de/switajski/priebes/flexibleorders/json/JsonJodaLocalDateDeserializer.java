package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

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
