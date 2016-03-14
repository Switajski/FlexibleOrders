package de.switajski.priebes.flexibleorders.service.conversion;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryNotesBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.InvoiceBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.InvoiceItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderConfirmationBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ShippingItemBuilder;

public class OverdueItemDtoServiceTest {

    // SUT
    ReportItemToItemDtoConversionService overdueItemDtoCreator;

    // Input use cases from "test data.ods"
    Order b11;
    OrderConfirmation ab11;
    DeliveryNotes l14;

    Order b15;
    OrderConfirmation ab15;

    DeliveryNotes l15;
    ShippingItem l15_si_b15;
    ShippingItem l15_si_b11;

    // Special cases:
    Invoice r15;

    DeliveryNotes l21;
    DeliveryNotes l22;
    DeliveryNotes l23;
    Invoice r21;

    // Output
    int overdue;

    @Before
    public void setup() {
        overdueItemDtoCreator = new ReportItemToItemDtoConversionService();
    }

    /**
     * extracted case "Paul" from test data.ods
     */
    private void givenTestData() {
        givenB11();
        givenB15();
        givenAB11();
        givenAB15();
        givenL14();
        givenL15();
    }

    @Test
    public void shouldCreateItemToBeShippedIfQtyOfConfirmationItemIsNotCoveredByShippingItems() {
        givenTestData();

        whenDeterminingOverdue(l14.getItems().iterator().next());

        assertThat(overdue, equalTo(5));
    }

    @Test
    public void shouldNotCreateItemToBeShippedIfQtyOfConfirmationItemIsCoveredByShippingItems() {
        givenTestData();

        whenDeterminingOverdue(ab15.getItems().iterator().next());

        assertThat(overdue, is(equalTo(0)));
    }

    @Test
    public void shouldCreateItemToBeInvoicedIfNoInvoiceItemExists() {
        givenTestData();

        whenDeterminingOverdue(l14.getItems().iterator().next());

        assertThat(overdue, equalTo(5));
    }

    @Test
    public void shouldCreateItemToBeInvoicedIfNoInvoiceItemExists2() {
        givenTestData();

        whenDeterminingOverdue(l15_si_b11);

        assertThat(overdue, equalTo(15));
    }

    @Test
    public void shouldCreateItemToBeInvoicedIfNoInvoiceItemExists3() {
        givenTestData();

        whenDeterminingOverdue(l15_si_b15);

        assertThat(overdue, equalTo(8));
    }

    @Test
    public void shouldNotCreateItemToBeInvoicedIfInvoiceItemWithSameQtyExists() {
        givenTestData();
        givenR15();

        whenDeterminingOverdue(l15_si_b11);

        assertThat(overdue, is(equalTo(0)));
    }

    @Test
    public void shouldCreateItemToBeInvoicedIfQtyOfShippingItemsAreNotCoveredByInvoiceItem() {
        givenTestData();
        givenR15();

        whenDeterminingOverdue(l14.getItems().iterator().next());

        assertThat(overdue, is(equalTo(5)));
    }

    @Test
    public void shouldHandleItemsToBeInvoicedIfInvoiceItemsExistsWithSameQuantity() {
        givenB11();
        givenL2xs();
        givenR21();

        int sumOverdue = 0;
        whenDeterminingOverdue(l21.getItems().iterator().next());
        sumOverdue += overdue;
        whenDeterminingOverdue(l22.getItems().iterator().next());
        sumOverdue += overdue;
        whenDeterminingOverdue(l23.getItems().iterator().next());
        sumOverdue += overdue;

        assertThat(sumOverdue, is(equalTo(2)));

    }

    void givenB11() {
        b11 = new OrderBuilder()
                .setCustomer(new CustomerBuilder().setId(1L).build())
                .setOrderNumber("B11")
                .addOrderItem(new OrderItem(b11, new Product(), 30))
                .build();

    }

    void givenL14() {
        l14 = new DeliveryNotesBuilder()
                .setDocumentNumber("L14")
                .addItem(new ShippingItemBuilder()
                        .setQuantity(5)
                        .setItem(orderItemOfB11())
                        .setPredecessor(ab11.getItems().iterator().next())
                        .build())
                .build();
    }

    OrderItem orderItemOfB11() {
        return b11.getItems().iterator().next();
    }

    void givenAB11() {
        ab11 = new OrderConfirmationBuilder()
                .setOrderAgreementNumber("AU11")
                .addItem(new ConfirmationItemBuilder()
                        .setItem(orderItemOfB11())
                        .setQuantity(30)
                        .build())
                .setDocumentNumber("AB11")
                .build();
    }

    void givenB15() {
        b15 = new OrderBuilder()
                .setCustomer(new CustomerBuilder().setId(1L).build())
                .setOrderNumber("B15")
                .addOrderItem(new OrderItem(b15, new Product(), 8))
                .build();

    }

    void givenAB15() {
        ab15 = new OrderConfirmationBuilder()
                .setOrderAgreementNumber("AU15")
                .setDocumentNumber("AB15")
                .addItem(new ConfirmationItemBuilder()
                        .setItem(b15.getItems().iterator().next())
                        .setQuantity(8)
                        .build())
                .build();
    }

    void givenL2xs() {
        l21 = createSingleDeliveryNotesForB11();
        l22 = createSingleDeliveryNotesForB11();
        l23 = createSingleDeliveryNotesForB11();
    }

    void givenR21() {
        r21 = new InvoiceBuilder()
                .setDocumentNumber("R15")
                .addItem(new InvoiceItemBuilder()
                        .setItem(orderItemOfB11())
                        .setPredecessor(l21.getItems().iterator().next())
                        .setQuantity(1)
                        .build())
                .build();
    }

    private DeliveryNotes createSingleDeliveryNotesForB11() {
        DeliveryNotes createSingleDeliveryNotesForB11 = new DeliveryNotesBuilder()
                .addItem(new ShippingItemBuilder()
                        .setItem(orderItemOfB11())
                        .setPredecessor(l15_si_b11)
                        .setQuantity(1)
                        .build())
                .build();
        return createSingleDeliveryNotesForB11;
    }

    void givenL15() {
        l15 = new DeliveryNotesBuilder().setDocumentNumber("L15").build();

        l15_si_b15 = new ShippingItemBuilder()
                .setReport(l15)
                .setItem(orderItemOfB15())
                .setPredecessor(ab15.getItems().iterator().next())
                .setQuantity(8)
                .build();
        l15.addItem(l15_si_b15);

        l15_si_b11 = new ShippingItemBuilder()
                .setReport(l15)
                .setItem(orderItemOfB11())
                .setPredecessor(ab11.getItems().iterator().next())
                .setQuantity(15)
                .build();
        l15.addItem(l15_si_b11);

    }

    void givenR15() {
        r15 = new InvoiceBuilder()
                .setDocumentNumber("R15")
                .addItem(new InvoiceItemBuilder()
                        .setItem(orderItemOfB11())
                        .setPredecessor(l15_si_b11)
                        .setQuantity(15)
                        .build())
                .build();
    }

    OrderItem orderItemOfB15() {
        return b15.getItems().iterator().next();
    }

    void whenDeterminingOverdue(ReportItem reportItem) {
        overdue = reportItem.overdue();
    }
}
