package de.switajski.priebes.flexibleorders.web;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;

public class JacksonSerializationTest {

    private String serialized;

    @Test
    public void shouldSerializeErrorsCorrectly() throws JsonGenerationException, JsonMappingException, IOException {

        JsonObjectResponse response = givenResponse();

        whenSerializing(response);

        String expectedSerialization = "{\"total\":0,\"success\":false,\"data\":null,\"message\":null,"
                + "\"errors\":{\"key 2\":\"value 2\",\"key 1\":\"value 1\"}}";
        assertThat(
                serialized,
                is(equalTo(expectedSerialization)));
        ;
    }

    private void whenSerializing(JsonObjectResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        serialized = mapper.writeValueAsString(response);
    }

    private JsonObjectResponse givenResponse() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key 1", "value 1");
        map.put("key 2", "value 2");

        JsonObjectResponse response = new JsonObjectResponse();
        response.setSuccess(false);
        response.setErrors(map);
        return response;
    }

}
