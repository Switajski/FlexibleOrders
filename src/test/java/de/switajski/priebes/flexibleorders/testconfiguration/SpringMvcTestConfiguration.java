package de.switajski.priebes.flexibleorders.testconfiguration;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SpringMvcTestConfiguration {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    FilterChainProxy springSecurityFilter;

    protected MockMvc mvcWithSecurity;

    protected MockMvc mvc;

    @Before
    public void setup() {
        mvcWithSecurity = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilter, "/*").build();
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

}
