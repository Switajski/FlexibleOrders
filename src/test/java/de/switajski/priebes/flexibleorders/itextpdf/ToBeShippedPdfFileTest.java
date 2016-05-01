package de.switajski.priebes.flexibleorders.itextpdf;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.itextpdf.dto.ToBeShippedInPdf;

public class ToBeShippedPdfFileTest {

    ToBeShippedInPdf reportDto;

    @Test
    public void shouldGenerateDeliveryNotesWithPendingItems() throws Exception {
        givenReportDtoModel();

        whenCreatingPdfFile("ToBeShippedPdfFileTest.pdf");
    }

    private void givenReportDtoModel() {
        reportDto = new ToBeShippedInPdf();
        ReportInPdfTestFixture.amendTestData(reportDto);
        reportDto.shippingSpecific_packageNumber = "1";
    }

    private void whenCreatingPdfFile(String fileName) throws Exception {
        new ItextPdfTestHelper().createPdfFile(fileName, reportDto);
    }

}
