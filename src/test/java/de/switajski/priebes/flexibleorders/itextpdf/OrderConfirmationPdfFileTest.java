package de.switajski.priebes.flexibleorders.itextpdf;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderConfirmationDto;

public class OrderConfirmationPdfFileTest {

    private OrderConfirmationDto reportDto;

    @Test
    public void shouldGenerateOrderConfirmation() throws Exception {

        reportDto = new OrderConfirmationDto();
        ReportDtoTestFixture.amendTestData(reportDto);

        whenCreatingPdfFile("OrderConfirmationPdfFile.pdf");
    }

    private void whenCreatingPdfFile(String fileName) throws Exception {
        new ItextPdfTestHelper().createPdfFile(fileName, reportDto);
    }
}
