package de.switajski.priebes.flexibleorders.testhelper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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

    protected void expectPdfIsRendering(String docNo) throws Exception {
        mvc.perform(post("/reports/" + docNo + ".pdf"))
                .andExpect(content().contentTypeCompatibleWith("application/pdf"));
    }

}
