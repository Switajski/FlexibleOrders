package de.switajski.priebes.flexibleorders.itextpdf;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.ToBeShippedPdfFile;

public class ToBeShippedPdfFileTest {

    String pdfPath = "src/test/resources/";

    ToBeShippedPdfFile deliveryNotesPdfFile;
    ReportDto reportDto;
    Map<String, Object> model;

    @Before
    public void setup() {
        deliveryNotesPdfFile = new ToBeShippedPdfFile();
        deliveryNotesPdfFile.setLogoPath("src/main/webapp/images/LogoGross.jpg");
    }

    @Test
    public void shouldGenerateDeliveryNotesWithPendingItems() throws Exception {
        deliveryNotesPdfFile.setFilePathAndName(pdfPath + "ToBeShippedPdfFileTest.pdf");
        givenReportDtoModel();

        whenCreatingPdfFile();
    }

    private void givenReportDtoModel() {
        reportDto = ReportDtoTestFixture.givenReportDto();
        reportDto.shippingSpecific_packageNumber = "1";

        model = new HashMap<String, Object>();
        model.put(reportDto.getClass().getSimpleName(), reportDto);
    }

    private void whenCreatingPdfFile() throws Exception {
        deliveryNotesPdfFile.render(
                model,
                new MockHttpServletRequest(),
                new MockHttpServletResponse());
    }

}
