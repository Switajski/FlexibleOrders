package de.switajski.priebes.flexibleorders.web;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.switajski.priebes.flexibleorders.testdata.StandardTestDataRule;

@Transactional
public class SpringMvcWithTestDataTestConfiguration extends SpringMvcTestConfiguration {

    @Rule
    @Autowired
    public StandardTestDataRule rule;

    /**
     * Create json string out of an object.
     * 
     * @param o
     * @return object in json
     * @throws JsonProcessingException
     */
    protected String createStringRequest(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

}
