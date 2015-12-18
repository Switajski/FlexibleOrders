package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import de.switajski.priebes.flexibleorders.reference.Country;

public class CountryDeserializer extends JsonDeserializer<Country> {

    @Override
    public Country deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String country = StringUtils.stripToNull(p.getText());
        return Country.map(country);
    }

}
