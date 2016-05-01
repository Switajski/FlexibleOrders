package de.switajski.priebes.flexibleorders.itextpdf;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;

public class OrderPdfFileTest {

    private Order order;

    private OrderInPdf reportDto;

    @Test
    public void shouldGenerateOrder() throws Exception {
        reportDto = new OrderInPdf();
        ReportDtoTestFixture.amendTestData(reportDto);
        addOrderItems(reportDto);

        whenCreatingPdfFile("OrderPdfFileTest.pdf");
    }

    private void addOrderItems(ReportInPdf dto) {
        dto.orderItems = new HashSet<OrderItem>(Arrays.asList(
                new OrderItemBuilder(
                        order,
                        CatalogProductBuilder
                                .buildWithGeneratedAttributes(98760)
                                .toProduct(),
                        5)
                        .generateAttributes(15)
                        .build(),
                new OrderItemBuilder(
                        order,
                        CatalogProductBuilder
                                .buildWithGeneratedAttributes(98760)
                                .toProduct(),
                        12)
                        .generateAttributes(12)
                        .build()));
    }

    private void whenCreatingPdfFile(String fileName) throws Exception {
        new ItextPdfTestHelper().createPdfFile(fileName, reportDto);
    }
}
