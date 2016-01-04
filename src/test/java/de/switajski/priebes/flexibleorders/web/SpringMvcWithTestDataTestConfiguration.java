package de.switajski.priebes.flexibleorders.web;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.testdata.StandardTestDataRule;

@Transactional
public class SpringMvcWithTestDataTestConfiguration extends SpringMvcTestConfiguration {

    @Rule
    @Autowired
    public StandardTestDataRule rule;

}
