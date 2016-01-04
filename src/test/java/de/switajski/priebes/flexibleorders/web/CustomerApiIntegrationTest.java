package de.switajski.priebes.flexibleorders.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import de.switajski.priebes.flexibleorders.web.dto.CustomerDto;

public class CustomerApiIntegrationTest extends SpringMvcWithTestDataTestConfiguration {

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

    @Test
    public void shouldCreateCustomer() throws Exception {
        CustomerDto customer = new CustomerDto();
        customer.customerNumber = 1234L;
        customer.companyName = "Flexible Inc.";

        mvc.perform(post("/customers/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createStringRequest(customer)))
                .andExpect(status().is2xxSuccessful());
    }
}
