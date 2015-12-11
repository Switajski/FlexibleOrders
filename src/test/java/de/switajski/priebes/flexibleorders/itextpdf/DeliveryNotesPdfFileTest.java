package de.switajski.priebes.flexibleorders.itextpdf;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.PendingItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.dto.DeliveryNotesDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ProductBuilder;

public class DeliveryNotesPdfFileTest {

    DeliveryNotesPdf deliveryNotesPdfFile;
    ReportDto reportDto;
    Map<String, Object> model;

    @Test
    public void shouldGenerateDeliveryNotesWithPendingItems() throws Exception {
        givenReportDtoModel();

        reportDto.items.add(givenPendingItem());

        whenCreatingPdfFile("DeliveryNotesWithPendingItems.pdf");
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
        givenReportDtoModel();

        whenCreatingPdfFile("DeliveryNotesWithoutPrices.pdf");
    }

    @Test
    public void shouldGenerateDeliveryNotesWithPackageNumbers() throws Exception {
        givenReportDtoModel();
        addPackageNumbers();

        whenCreatingPdfFile("DeliveryNotesWithPackageNumbers.pdf");
    }

    @Test
    public void shouldGenerateDeliveryNotesWithPackageNumbersAndPrices() throws Exception {
        givenReportDtoModel();
        reportDto.showPricesInDeliveryNotes = true;
        addPackageNumbers();

        whenCreatingPdfFile("DeliveryNotesWithPackageNumbersAndPrices.pdf");
    }

    @Test
    public void shouldGenerateDeliveryNotesWithPrices() throws Exception {
        givenReportDtoModel();
        reportDto.showPricesInDeliveryNotes = true;

    }

    private void addPackageNumbers() {
        for (ReportItem ri : reportDto.items) {
            if (ri instanceof ShippingItem) {
                ((ShippingItem) ri).setPackageNumber("P12");
            }
        }
    }

    private void whenCreatingPdfFile(String fileName) throws Exception {
        new ItextPdfTestHelper().createPdfFile(fileName, reportDto);
    }

    private void givenReportDtoModel() {
        reportDto = ReportDtoTestFixture.amendTestData(new DeliveryNotesDto());
        reportDto.shippingSpecific_packageNumber = "1";

        model = new HashMap<String, Object>();
        model.put(reportDto.getClass().getSimpleName(), reportDto);
    }

}
