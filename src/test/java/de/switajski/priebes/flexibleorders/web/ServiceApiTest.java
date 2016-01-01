package de.switajski.priebes.flexibleorders.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.switajski.priebes.flexibleorders.web.dto.CustomerDto;
import de.switajski.priebes.flexibleorders.web.dto.JsonCreateReportRequest;

public class ServiceApiTest extends SpringMvcTestConfiguration {

    @Test
    public void shouldBeRejectedByValidation() throws Exception {
        JsonCreateReportRequest request = new JsonCreateReportRequest();
        request.setCustomerId(45L);

        mvc.perform(post("/transitions/order")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createStringRequest(request)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("\"errors\":{\"items\":")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldValidateCustomer() throws Exception {
        CustomerDto customer = new CustomerDto();
        customer.customerNumber = 123L;

        mvc.perform(post("/customers/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createStringRequest(customer)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("errors")))
                .andExpect(content().string(containsString("companyName")))
                .andExpect(status().is4xxClientError());

    }

    private String createStringRequest(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}
