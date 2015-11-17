package de.switajski.priebes.flexibleorders.itextpdf;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.InvoicePdfFile;

public class InvoicePdfFileTest {

    private static final String INVOICE_PDF_PATH = "src/test/resources/InvoicePdfFileTest.pdf";

    @Transactional(readOnly = true)
    @Test
    public void shouldGenerateInvoice() throws Exception {

        InvoicePdfFile bpf = new InvoicePdfFile();
        bpf.setFilePathAndName(INVOICE_PDF_PATH);
        bpf.setLogoPath("src/main/webapp/images/LogoGross.jpg");

        Map<String, Object> model = new HashMap<String, Object>();
        ReportDto reportDto = ReportDtoTestFixture.givenReportDto();
        reportDto.invoiceSpecific_discountRate = BigDecimal.ONE;
        reportDto.invoiceSpecific_discountText = "Discount 0.5 + 0.5 %";
        reportDto.documentNumber = "R123456678";
        reportDto.related_orderNumbers = new HashSet<String>(Arrays.asList("B12345678", "B22345678", "B12345679"));
        reportDto.related_orderConfirmationNumbers = new HashSet<String>(Arrays.asList("AB12345678", "AB22345678", "AB12345679"));
        reportDto.related_deliveryNotesNumbers = new HashSet<String>(Arrays.asList("L12345678", "L22345678", "L12345679"));
        model.put(reportDto.getClass().getSimpleName(), reportDto);

        bpf.render(
                model,
                new MockHttpServletRequest(),
                new MockHttpServletResponse());

    }
}
