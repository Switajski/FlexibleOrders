package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.specification.AgreedItemsToBeShippedSpec;
import de.switajski.priebes.flexibleorders.repository.specification.ConfirmationItemToBeAgreedSpec;
import de.switajski.priebes.flexibleorders.service.api.AgreeingService;
import de.switajski.priebes.flexibleorders.service.api.ConfirmingService;
import de.switajski.priebes.flexibleorders.service.api.OrderingService;
import de.switajski.priebes.flexibleorders.service.api.ShippingService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testhelper.AbstractSpringContextTest;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Transactional
public class SpecificationIntegrationTest extends AbstractSpringContextTest {

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private CatalogProductServiceByMagento productService;

    @Autowired
    private OrderingService orderingService;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ItemDtoConverterService itemDtoConverterService;

    @Autowired
    private ReportItemRepository reportItemRepository;

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private AgreeingService agreeingService;

    private ConfirmingService confirmingService;

    private static final int JUREK_QTY = 5; // All Jureks from B12

    private static final Product AMY = new CatalogProductBuilder()
    .amy().build()
    .toProduct();

    private static final Product PAUL = new CatalogProductBuilder()
    .paul().build()
    .toProduct();

    private static final Product MILADKA = new CatalogProductBuilder()
    .miladka().build()
    .toProduct();

    private static final Product SALOME = new CatalogProductBuilder()
    .salome().build()
    .toProduct();

    private static final Product JUREK = new CatalogProductBuilder()
    .jurek().build()
    .toProduct();

    @Transactional
    @Test
    public void confirmationItemToBeAgreedSpecificationShouldReturnAgreedItemsOnly() {
        // GIVEN
        givenDefinedTestData();

        // WHEN
        List<ReportItem> retrievedRis = reportItemRepository
                .findAll(new ConfirmationItemToBeAgreedSpec());

        // THEN
        assertAllItemsAreAgreed(retrievedRis);
    }

    /**
     * test data from Open Office calculation sheet "Test Data.ods"
     */
    private void givenDefinedTestData() {
        if (orderRepo.findByOrderNumber("B11") != null) {
            OrderConfirmation agreement = givenOrderAgreement();
            givenDeliveryReports(agreement);
        }
    }

    @Transactional
    @Test
    public void agreedItemsToBeShippedSpecShouldReturnAgreedItemsOnly() {
        givenDefinedTestData();

        // WHEN
        List<ReportItem> retrievedRis = reportItemRepository
                .findAll(new AgreedItemsToBeShippedSpec());

        // THEN
        assertAllItemsAreAgreed(retrievedRis);
        assertConfirmationItemHaveNoJurek(retrievedRis);
    }

    private void assertAllItemsAreAgreed(
            List<ReportItem> retrievedRis) {
        for (ReportItem ri : retrievedRis) {
            assertThat(ri instanceof ConfirmationItem, is(true));
            assertThat(((ConfirmationItem) ri).isAgreed(), is(true));
        }
    }

    private void assertConfirmationItemHaveNoJurek(List<ReportItem> retrievedRis) {
        for (ReportItem ri : retrievedRis) {
            assertThat(ri.getOrderItem().getProduct(), is(not(equalTo(JUREK))));
        }
    }

    /**
     * L11
     * <ul>
     * <li>2 Amy</li>
     * <li>2 Miladka</li>
     * </ul>
     * L12
     * <ul>
     * <li>3 Amy</li>
     * <li>3 Miladka</li>
     * </ul>
     * L13
     * <ul>
     * <li>1 Salome</li>
     * <li>5 Jurek</li>
     * </ul>
     * L14
     * <ul>
     * <li>5 Paul</li>
     * </ul>
     *
     * @param orderAgreement
     */
    private void givenDeliveryReports(OrderConfirmation orderAgreement) {
        DeliverParameter deliverParameterL11 = new DeliverParameter("L11", new Date(), Arrays.asList(
                createItemDto(2, AMY, orderAgreement),
                createItemDto(2, MILADKA, orderAgreement)));
        deliverParameterL11.trackNumber = "trackNumber";
        deliverParameterL11.packageNumber = "packNo";
        deliverParameterL11.showPricesInDeliveryNotes = false;
        shippingService.ship(
                deliverParameterL11);

        DeliverParameter deliverParameterL12 = new DeliverParameter("L12", new Date(), Arrays.asList(
                createItemDto(3, AMY, orderAgreement),
                createItemDto(3, MILADKA, orderAgreement)));
        deliverParameterL12.trackNumber = "trackNumber";
        deliverParameterL12.packageNumber = "packNo";
        deliverParameterL12.showPricesInDeliveryNotes = false;
        shippingService.ship(
                deliverParameterL12);

        DeliverParameter deliverParameterL13 = new DeliverParameter("L13", new Date(), Arrays.asList(
                createItemDto(1, SALOME, orderAgreement),
                createItemDto(JUREK_QTY, JUREK, orderAgreement)));
        deliverParameterL13.trackNumber = "trackNumber";
        deliverParameterL13.packageNumber = "packNo";
        deliverParameterL13.showPricesInDeliveryNotes = false;
        shippingService.ship(deliverParameterL13);

        DeliverParameter deliverParameter14 = new DeliverParameter("L14", new Date(), Arrays.asList(
                createItemDto(5, PAUL, orderAgreement)));
        deliverParameter14.trackNumber = "trackNumber";
        deliverParameter14.packageNumber = "packNo";
        deliverParameter14.showPricesInDeliveryNotes = false;
        shippingService.ship(deliverParameter14);
    }

    private ItemDto createItemDto(int qty, Product product,
            OrderConfirmation agreement) {
        ItemDto item = itemDtoConverterService.convert(
                getFirstItemOf(
                        product,
                        agreement));
        item.quantityLeft = qty;
        return item;
    }

    private ReportItem getFirstItemOf(Product product, OrderConfirmation report) {
        ReportItem specificReportItem = null;
        for (ReportItem ri : report.getItems()) {
            if (ri.getOrderItem().getProduct().equals(product)) {
                specificReportItem = ri;
                break;
            }
        }
        return specificReportItem;
    }

    /**
     * B11:
     * <ul>
     * <li>10 Amy</li>
     * <li>15 Miladka</li>
     * <li>30 Paul</li>
     * </ul>
     *
     * B12:
     * <ul>
     * <li>12 Salome</li>
     * <li>5 Jurek</li>
     * </ul>
     *
     * AB11:
     * <ul>
     * <li>10 Amy</li>
     * <li>15 Miladka</li>
     * <li>30 Paul</li>
     * <li>12 Salome</li>
     * <li>5 Jurek</li>
     * </ul>
     *
     * AU11: </br> same as AB11
     *
     * @return
     */
    private OrderConfirmation givenOrderAgreement() {
        Order b11 = OrderBuilder.B11();
        Customer yvonne = customerService.create(b11.getCustomer());
        orderRepo.save(b11);

        Order b12 = OrderBuilder.B12();
        b12.setCustomer(yvonne);
        orderRepo.save(b12);

        List<ItemDto> b11AndB12 = itemDtoConverterService.convert(b11);
        b11AndB12.addAll(itemDtoConverterService.convert(b12));

        OrderConfirmation confirmationReport = confirmingService.confirm(
                new ConfirmParameter(
                        b11.getOrderNumber(),
                        "AB11",
                        new LocalDate(),
                        null,
                        AddressBuilder.createDefault(),
                        AddressBuilder.createDefault(),
                        b11AndB12));

        confirmationReport = agreeingService.agree(confirmationReport.getDocumentNumber(), "AU11");
        return confirmationReport;
    }
}
