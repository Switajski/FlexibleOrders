package de.switajski.priebes.flexibleorders.testconfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SpringMvcWithTestDataTestConfiguration extends SpringMvcTestConfiguration {

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
