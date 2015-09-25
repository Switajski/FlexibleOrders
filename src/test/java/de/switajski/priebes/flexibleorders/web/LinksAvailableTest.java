package de.switajski.priebes.flexibleorders.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

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
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath*:org/flexible/order/application-context*.xml" })
public class LinksAvailableTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilter;

    private MockMvc mvcWithSecurity;
    
    private MockMvc mvc;
    
    @Before
    public void setup(){
        mvcWithSecurity = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilter, "/*").build();
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldBeForbidden() throws Exception {
        mvcWithSecurity
                .perform(get("/FlexibleOrders/ordered").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void urlShouldBeAvailable() throws Exception {
        mvc.perform(get("/FlexibleOrders/ordered").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
    
    @Test
    public void shouldLogin() throws Exception {
        mvcWithSecurity.perform(get("/admin").with(user("admin").password("pass").roles("USER","ADMIN")))
            .andExpect(status().is2xxSuccessful());
    }
    
}
