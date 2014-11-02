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
import de.switajski.priebes.flexibleorders.service.api.AgreementService;
import de.switajski.priebes.flexibleorders.service.api.DeliveryService;
import de.switajski.priebes.flexibleorders.service.api.OrderService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testhelper.AbstractSpringContextTest;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class SpecificationIntegrationTest extends AbstractSpringContextTest {

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private CatalogProductServiceImpl productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ItemDtoConverterService itemDtoConverterService;

    @Autowired
    private ReportItemRepository reportItemRepository;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private AgreementService agreementService;

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
    public void findAll_OpenShippingItemSpecShouldRetrieveSpecifiedReportItems() {
        // GIVEN test data from Open Office calculation sheet "Test Data.ods"
        OrderConfirmation agreement = givenOrderAgreement();
        givenDeliveryReports(agreement);

        // WHEN
        List<ReportItem> retrievedRis = reportItemRepository
                .findAll(new ConfirmationItemToBeAgreedSpec());

        // THEN
        assertAllItemsAreAgreed(retrievedRis);
    }

    @Transactional
    @Test
    public void findAll_AgreedItemsToBeShipped() {
        // GIVEN test data from Open Office calculation sheet "Test Data.ods"
        OrderConfirmation agreement = givenOrderAgreement();
        givenDeliveryReports(agreement);

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
        deliveryService.deliver(
                new DeliverParameter("L11", "trackNumber", "packNo", null, new Date(), Arrays.asList(
                        createItemDto(2, AMY, orderAgreement),
                        createItemDto(2, MILADKA, orderAgreement))));

        deliveryService.deliver(
                new DeliverParameter("L12", "trackNumber", "packNo", null, new Date(), Arrays.asList(
                        createItemDto(3, AMY, orderAgreement),
                        createItemDto(3, MILADKA, orderAgreement))));

        deliveryService.deliver(
                new DeliverParameter("L13", "trackNumber", "packNo", null, new Date(), Arrays.asList(
                        createItemDto(1, SALOME, orderAgreement),
                        createItemDto(JUREK_QTY, JUREK, orderAgreement))));

        deliveryService.deliver(
                new DeliverParameter("L14", "trackNumber", "packNo", null, new Date(), Arrays.asList(
                        createItemDto(5, PAUL, orderAgreement))));
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

        OrderConfirmation confirmationReport = orderService.confirm(
                new ConfirmParameter(
                        b11.getOrderNumber(),
                        "AB11",
                        new LocalDate(),
                        null,
                        AddressBuilder.createDefault(),
                        AddressBuilder.createDefault(),
                        b11AndB12));

        confirmationReport = agreementService.agree(confirmationReport.getDocumentNumber(), "AU11");
        return confirmationReport;
    }
}
