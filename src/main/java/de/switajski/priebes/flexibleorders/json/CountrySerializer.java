package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.switajski.priebes.flexibleorders.reference.Country;

public class CountrySerializer extends JsonSerializer<Country> {

    @Override
    public void serialize(Country value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeString(value.toString());

    }

}
