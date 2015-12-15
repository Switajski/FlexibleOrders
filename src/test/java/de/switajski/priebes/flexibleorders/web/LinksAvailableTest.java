package de.switajski.priebes.flexibleorders.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath*:org/flexible/order/application-context*.xml", "classpath*:servlet*.xml" })
public class LinksAvailableTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilter;

    private MockMvc mvcWithSecurity;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvcWithSecurity = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilter, "/*").build();
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldBeForbidden() throws Exception {
        mvcWithSecurity
                .perform(get("/reportitems/ordered").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnBadRequestOnInvalidRequest() throws Exception {
        mvc.perform(get("/reportitems/ordered").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void shouldReturnSuccess() throws Exception {
        mvc.perform(get("/reportitems/ordered")
                .param("page", "1")
                .param("limit", "50")
                ).andExpect(
                        status().isOk());
    }

}
