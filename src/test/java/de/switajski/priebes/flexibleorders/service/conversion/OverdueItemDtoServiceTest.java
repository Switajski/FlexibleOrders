package de.switajski.priebes.flexibleorders.service.conversion;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryNotesBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderConfirmationBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ShippingItemBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class OverdueItemDtoServiceTest {

    // SUT
    OverdueItemDtoService overdueItemDtoCreator;

    // Input
    Order b11;
    OrderItem b11_oi;
    ConfirmationItem ab11_ci;
    ShippingItem l14_si;
    ShippingItem l15_si_b11;
    DeliveryNotes l14;
    DeliveryNotes l15;

    Order b15;
    OrderItem b15_oi;
    ConfirmationItem ab15_ci;
    ShippingItem l15_si_b15;

    // Output
    ItemDto itemDto;

    @Before
    public void setup() {
        overdueItemDtoCreator = new OverdueItemDtoService();
        givenOrderItemB11();
        givenOrderItemB15();
    }

    @Test
    public void confirmationItemFromAB11ShouldHaveOverdue() {

        whenConverting(ab11_ci);

        assertThat(itemDto.quantity, equalTo(8));
    }

    @Test
    public void shippingItemFromL14ShouldHaveOverdue() {

        whenConverting(l14_si);

        assertThat(itemDto.quantity, equalTo(5));
    }

    @Test
    public void shippingItemFromL15AndB11ShouldHaveOverdue() {

        whenConverting(l15_si_b11);

        assertThat(itemDto.quantity, equalTo(15));
    }

    @Test
    public void shippingItemFromL15AndB15ShouldHaveOverdue() {

        whenConverting(l15_si_b15);

        assertThat(itemDto.quantity, equalTo(8));
    }

    private void givenOrderItemB11() {

        b11 = new OrderBuilder()
                .setCustomer(new CustomerBuilder().setId(1L).build())
                .setOrderNumber("B11")
                .build();
        b11_oi = new OrderItem(b11, new Product(), 0);

        ab11_ci = new ConfirmationItemBuilder()
                .setItem(b11_oi)
                .setQuantity(30)
                .setReport(new OrderConfirmationBuilder()
                        .setOrderAgreementNumber("AU11")
                        .setDocumentNumber("AB11")
                        .build())
                .build();

        l14 = new DeliveryNotesBuilder().setDocumentNumber("L14").build();
        l14_si = new ShippingItemBuilder()
                .setReport(l14)
                .setQuantity(5)
                .setItem(b11_oi)
                .build();

        l15_si_b11 = new ShippingItemBuilder()
                .setReport(l14)
                .setQuantity(15)
                .setItem(b11_oi)
                .build();

    }

    private void givenOrderItemB15() {
        b15 = new OrderBuilder()
                .setCustomer(new CustomerBuilder().setId(1L).build())
                .setOrderNumber("B15")
                .build();

        b15_oi = new OrderItem(b15, new Product(), 0);

        ab15_ci = new ConfirmationItemBuilder()
                .setItem(b15_oi)
                .setQuantity(8)
                .setReport(new OrderConfirmationBuilder()
                        .setOrderAgreementNumber("AU15")
                        .setDocumentNumber("AB15")
                        .build())
                .build();

        l15 = new DeliveryNotesBuilder().setDocumentNumber("L15").build();
        l15_si_b15 = new ShippingItemBuilder()
                .setReport(l15)
                .setItem(b15_oi)
                .setQuantity(8)
                .build();

    }

    private void whenConverting(ReportItem reportItem) {
        itemDto = overdueItemDtoCreator.createOverdue(reportItem);
    }
}
