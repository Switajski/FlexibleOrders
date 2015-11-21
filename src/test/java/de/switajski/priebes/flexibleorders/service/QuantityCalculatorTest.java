package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.service.conversion.QuantityToBeProcessedDeterminator;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryNotesBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ShippingItemBuilder;

public class QuantityCalculatorTest {

    private static final int QTY_PROCESSED = 5;
    private static final int QTY = 7;
    private OrderItem orderItem;
    private Address address = AddressBuilder.createDefault();

    Integer calculatedQuantity;

    @Test
    public void quantityToBeConfirmedShouldBeOrderedMinusConfirmed() {
        // GIVEN
        orderItem = givenOrderItem(QTY);
        orderItem.addReportItem(givenAgreedItem(QTY_PROCESSED));

        // WHEN
        whenCalculatingQtyToConfirmed();

        // THEN
        assertThat(calculatedQuantity, is(QTY - QTY_PROCESSED));
    }

    private void whenCalculatingQtyToConfirmed() {
        calculatedQuantity = orderItem.toBeConfirmed();
    }

    @Test
    public void quantityToBeShippedShouldAgreedMinusShipped() {
        // GIVEN
        orderItem = givenOrderItem(QTY);
        orderItem.addReportItem(givenAgreedItem(QTY));
        orderItem.addReportItem(givenDeliveryItem(QTY_PROCESSED));

        // WHEN
        whenCalculatingQtyToBeShipped();

        // THEN
        assertThat(calculatedQuantity, is(QTY - QTY_PROCESSED));
    }

    private void whenCalculatingQtyToBeShipped() {
        calculatedQuantity = new QuantityToBeProcessedDeterminator(orderItem.getConfirmationItems().iterator().next()).overdueQuantity();
    }

    private ConfirmationItem givenAgreedItem(int quantityProcessed) {
        return new ConfirmationItemBuilder()
                .setReport(givenConfirmationReport())
                .setQuantity(quantityProcessed)
                .setItem(orderItem)
                .build();
    }

    private ReportItem givenDeliveryItem(int qtyProcessed) {
        return new ShippingItemBuilder()
                .setReport(givenDeliveryNotes())
                .setQuantity(qtyProcessed)
                .setItem(orderItem)
                .build();
    }

    private Report givenDeliveryNotes() {
        return new DeliveryNotesBuilder().setDocumentNumber("L-123").build();
    }

    private OrderItem givenOrderItem(int quantity) {
        return new OrderItemBuilder(
                givenOrder(),
                givenProduct(),
                quantity)
                .build();
    }

    private OrderConfirmation givenConfirmationReport() {
        OrderConfirmation orderConfirmation = new OrderConfirmation("AB-123", address, address);
        orderConfirmation.setOrderAgreementNumber("AU-123");
        return orderConfirmation;
    }

    private Product givenProduct() {
        return new CatalogProductBuilder(
                "pro",
                "234",
                ProductType.PRODUCT)
                .build().toProduct();
    }

    private Order givenOrder() {
        return new OrderBuilder()
                .setCustomer(CustomerBuilder.buildWithGeneratedAttributes(2))
                .build();
    }
}
