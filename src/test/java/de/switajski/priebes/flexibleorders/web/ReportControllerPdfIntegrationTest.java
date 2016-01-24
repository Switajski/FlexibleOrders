package de.switajski.priebes.flexibleorders.web;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.testhelper.SpringMvcWithTestDataTestConfiguration;

public class ReportControllerPdfIntegrationTest extends SpringMvcWithTestDataTestConfiguration {

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

}
