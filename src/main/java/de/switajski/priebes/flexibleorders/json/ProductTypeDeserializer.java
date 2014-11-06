package de.switajski.priebes.flexibleorders.json;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import de.switajski.priebes.flexibleorders.reference.ProductType;

public class ProductTypeDeserializer extends JsonDeserializer<ProductType>{

    @Override
    public ProductType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String ptString = StringUtils.capitalize(jp.getText());;
        if (StringUtils.isEmpty(ptString))
            return null;
        return ProductType.mapFromString(ptString);
    }

}
