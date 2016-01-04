package de.switajski.priebes.flexibleorders.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.testdata.StandardTestDataRule;

@Transactional
public class ReportControllerPdfIntegrationTest extends SpringMvcTestConfiguration {

    @Rule
    @Autowired
    public StandardTestDataRule rule;

    @Test
    public void orderConfirmationPdfShouldBeAvailable() throws Exception {
        expectPdfIsRendering("AB11");
    }

    @Test
    public void orderPdfShouldBeAvailable() throws Exception {
        expectPdfIsRendering("B11");
    }

    @Test
    public void deliveryNotesPdfShouldBeAvailable() throws Exception {
        expectPdfIsRendering("L11");
    }

    @Test
    public void invoicePdfShouldBeAvailable() throws Exception {
        expectPdfIsRendering("R11");
    }

    private void expectPdfIsRendering(String docNo) throws Exception {
        mvc.perform(post("/reports/" + docNo + ".pdf"))
                .andExpect(content().contentTypeCompatibleWith("application/pdf"));
    }

}
