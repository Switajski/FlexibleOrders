package de.switajski.priebes.flexibleorders.itextpdf;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.PendingItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ProductBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.DeliveryNotesPdfFile;

public class DeliveryNotesPdfFileTest {

    String pdfPath = "src/test/resources/";

    DeliveryNotesPdfFile deliveryNotesPdfFile;
    ReportDto reportDto;
    Map<String, Object> model;

    @Before
    public void setup() {
        deliveryNotesPdfFile = new DeliveryNotesPdfFile();
        deliveryNotesPdfFile.setLogoPath("src/main/webapp/images/LogoGross.jpg");
    }

    @Test
    public void shouldGenerateDeliveryNotesWithPendingItems() throws Exception {
        deliveryNotesPdfFile.setFilePathAndName(pdfPath + "DeliveryNotesWithPendingItems.pdf");
        givenReportDtoModel();
        reportDto.items.add(givenPendingItem());

        whenCreatingPdfFile();
    }

    private PendingItem givenPendingItem() {
        PendingItem pendingItem = new PendingItem();
        pendingItem.setQuantity(1);
        OrderItem item = new OrderItem(
                new OrderBuilder().setOrderNumber("765").build(),
                new ProductBuilder().setProductNumber("123").setName("always pending").build(),
                3);
        pendingItem.setOrderItem(item);
        return pendingItem;
    }

    @Test
    public void shouldGenerateDeliveryNotesWithoutPrices() throws Exception {
        deliveryNotesPdfFile.setFilePathAndName(pdfPath + "DeliveryNotesWithoutPrices.pdf");
        givenReportDtoModel();

        whenCreatingPdfFile();
    }

    @Test
    public void shouldGenerateDeliveryNotesWithPackageNumbers() throws Exception {
        deliveryNotesPdfFile.setFilePathAndName(pdfPath + "DeliveryNotesWithPackageNumbers.pdf");
        givenReportDtoModel();
        addPackageNumbers();

        whenCreatingPdfFile();
    }

    @Test
    public void shouldGenerateDeliveryNotesWithPackageNumbersAndPrices() throws Exception {
        deliveryNotesPdfFile.setFilePathAndName(pdfPath + "DeliveryNotesWithPackageNumbersAndPrices.pdf");
        givenReportDtoModel();
        reportDto.showPricesInDeliveryNotes = true;
        addPackageNumbers();

        whenCreatingPdfFile();
    }

    @Test
    public void shouldGenerateDeliveryNotesWithPrices() throws Exception {
        deliveryNotesPdfFile.setFilePathAndName(pdfPath + "DeliveryNotesWithPrices.pdf");
        givenReportDtoModel();
        reportDto.showPricesInDeliveryNotes = true;

        whenCreatingPdfFile();
    }

    private void addPackageNumbers() {
        for (ReportItem ri : reportDto.items) {
            if (ri instanceof ShippingItem) {
                ((ShippingItem) ri).setPackageNumber("P12");
            }
        }
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
