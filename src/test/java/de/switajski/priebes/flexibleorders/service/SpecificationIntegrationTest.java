package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
import de.switajski.priebes.flexibleorders.service.api.ShippingService;
import de.switajski.priebes.flexibleorders.service.api.TransitionsService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoToReportItemConversionService;
import de.switajski.priebes.flexibleorders.service.conversion.OrderItemToItemDtoConversionService;
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testhelper.AbstractSpringContextTestConfiguration;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Transactional
public class SpecificationIntegrationTest extends AbstractSpringContextTestConfiguration {

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private CatalogProductServiceByMagento productService;

    @Autowired
    private TransitionsService orderingService;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ItemDtoToReportItemConversionService itemDtoConverterService;

    @Autowired
    private ReportItemToItemDtoConverterService ri2ItemDtoConversionService;

    @Autowired
    private OrderItemToItemDtoConversionService oi2ItemDtoConversionService;

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
        DeliverParameter deliverParameterL11 = new DeliverParameter("L11", LocalDate.now(), Arrays.asList(
                createItemDto(2, AMY, orderAgreement),
                createItemDto(2, MILADKA, orderAgreement)));
        amendStandardValues(deliverParameterL11);
        shippingService.ship(deliverParameterL11);

        DeliverParameter deliverParameterL12 = new DeliverParameter("L12", LocalDate.now(), Arrays.asList(
                createItemDto(3, AMY, orderAgreement),
                createItemDto(3, MILADKA, orderAgreement)));
        amendStandardValues(deliverParameterL12);
        shippingService.ship(deliverParameterL12);

        DeliverParameter deliverParameterL13 = new DeliverParameter("L13", LocalDate.now(), Arrays.asList(
                createItemDto(1, SALOME, orderAgreement),
                createItemDto(JUREK_QTY, JUREK, orderAgreement)));
        amendStandardValues(deliverParameterL13);
        shippingService.ship(deliverParameterL13);

        DeliverParameter deliverParameter14 = new DeliverParameter("L14", LocalDate.now(), Arrays.asList(
                createItemDto(5, PAUL, orderAgreement)));
        amendStandardValues(deliverParameter14);
        shippingService.ship(deliverParameter14);
    }

    private void amendStandardValues(DeliverParameter deliverParameterL11) {
        deliverParameterL11.setTrackNumber("trackNumber");
        deliverParameterL11.setPackageNumber("packNo");
        deliverParameterL11.setShowPricesInDeliveryNotes(false);
    }

    private ItemDto createItemDto(int qty, Product product,
            OrderConfirmation agreement) {
        ItemDto item = ri2ItemDtoConversionService.createOverdue(
                getFirstItemOf(
                        product,
                        agreement));
        item.setQuantityLeft(qty);
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

        List<ItemDto> b11AndB12 = oi2ItemDtoConversionService.convert(b11);
        b11AndB12.addAll(oi2ItemDtoConversionService.convert(b12));

        OrderConfirmation confirmationReport = confirmingService.confirm(
                new ConfirmParameter(
                        b11.getOrderNumber(),
                        "AB11",
                        LocalDate.now(),
                        null,
                        AddressBuilder.createDefault(),
                        AddressBuilder.createDefault(),
                        b11AndB12));

        confirmationReport = agreeingService.agree(confirmationReport.getDocumentNumber(), "AU11");
        return confirmationReport;
    }
}
