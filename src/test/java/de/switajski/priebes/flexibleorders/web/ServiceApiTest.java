package de.switajski.priebes.flexibleorders.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.switajski.priebes.flexibleorders.web.dto.JsonCreateReportRequest;

public class ServiceApiTest extends SpringMvcTestConfiguration {

    @Test
    public void shouldBeRejectedByValidation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonCreateReportRequest request = new JsonCreateReportRequest();
        request.setCustomerId(45L);

        mvc.perform(post("/transitions/order")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("\"errors\":{\"items\":")))
                .andExpect(status().is4xxClientError());

    }
}
