package de.switajski.priebes.flexibleorders.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

public class LinksAvailableTest extends SpringMvcTestConfiguration {

    @Test
    public void shouldBeForbidden() throws Exception {
        mvcWithSecurity.perform(get("/reportitems/ordered")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnBadRequestOnInvalidRequest() throws Exception {
        mvc.perform(get("/reportitems/ordered")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void shouldReturnSuccess() throws Exception {
        mvc.perform(get("/country"))
                .andExpect(status().isOk());
    }

}
