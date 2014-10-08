package de.switajski.priebes.flexibleorders.itextpdf;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.OrderConfirmationPdfFile;

public class OrderConfirmationPdfFileTest {

    private static final String OC_PDF_FILE = "src/test/resources/ConfirmationReportPdfFileTest.pdf";
    
    @Transactional
    @Test
    public void shouldGenerateOrderConfirmation() throws Exception {

        OrderConfirmationPdfFile bpf = new OrderConfirmationPdfFile();
        bpf.setFilePathAndName(OC_PDF_FILE);
        bpf.setLogoPath("C:/workspaces/gitRepos/FlexibleOrders/src/main/webapp/images/LogoGross.jpg");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ReportDto.class.getSimpleName(), ReportDtoTestFixture.givenReportDto());

        bpf.render(
                model,
                new MockHttpServletRequest(),
                new MockHttpServletResponse());

    }

}
