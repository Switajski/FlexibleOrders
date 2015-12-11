package de.switajski.priebes.flexibleorders.itextpdf;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.itextpdf.dto.InvoiceDto;

public class InvoicePdfFileTest {

    private InvoiceDto reportDto;

    @Transactional(readOnly = true)
    @Test
    public void shouldGenerateInvoice() throws Exception {

        reportDto = new InvoiceDto();

        ReportDtoTestFixture.amendTestData(reportDto);
        reportDto.invoiceSpecific_discountRate = BigDecimal.ONE;
        reportDto.invoiceSpecific_discountText = "Discount 0.5 + 0.5 %";
        reportDto.documentNumber = "R123456678";
        reportDto.related_orderNumbers = new HashSet<String>(Arrays.asList("B12345678", "B22345678", "B12345679"));
        reportDto.related_orderConfirmationNumbers = new HashSet<String>(Arrays.asList("AB12345678", "AB22345678", "AB12345679"));
        reportDto.related_deliveryNotesNumbers = new HashSet<String>(Arrays.asList("L12345678", "L22345678", "L12345679"));

        whenCreatingPdfFile("InvoicePdfFileTest.pdf");
    }

    private void whenCreatingPdfFile(String fileName) throws Exception {
        new ItextPdfTestHelper().createPdfFile(fileName, reportDto);
    }
}
