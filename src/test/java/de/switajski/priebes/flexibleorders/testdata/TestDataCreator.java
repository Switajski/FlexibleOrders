package de.switajski.priebes.flexibleorders.testdata;

import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.AB11;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.AB13;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.AB15;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.AMY;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.B11;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.B12;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.B13;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.B15;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.B21;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.B22;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.DHL;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.EDWARD;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.JEROME;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.JUREK;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.MILADKA;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.NAIDA;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.PAUL;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.SALOME;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.UPS;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.WYOMING;
import static de.switajski.priebes.flexibleorders.testdata.TestDataFixture.YVONNE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.CatalogDeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.CatalogDeliveryMethodRepository;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.service.api.AgreeingService;
import de.switajski.priebes.flexibleorders.service.api.ConfirmingService;
import de.switajski.priebes.flexibleorders.service.api.InvoicingParameter;
import de.switajski.priebes.flexibleorders.service.api.InvoicingService;
import de.switajski.priebes.flexibleorders.service.api.ShippingService;
import de.switajski.priebes.flexibleorders.service.api.TransitionsService;
import de.switajski.priebes.flexibleorders.service.conversion.OrderItemToItemDtoConversionService;
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

/**
 * Creates test data in order to ease GUI Testing.
 *
 * @author Marek Switajski
 *
 */
public class TestDataCreator {

    private CatalogProductRepository cpRepo;

    private CustomerRepository cRepo;

    private TransitionsService orderingService;

    private ConfirmingService confirmingService;

    private OrderItemToItemDtoConversionService oi2ItemDtoConversionService;

    private ReportItemToItemDtoConverterService riToItemConversionService;

    private ShippingService shippingService;

    private InvoicingService invoicingService;

    private CatalogDeliveryMethodRepository deliveryMethodRepo;

    private AgreeingService agreeingService;

    public TestDataCreator(
            CatalogProductRepository cpRepo,
            CustomerRepository cRepo,
            TransitionsService orderingService,
            ConfirmingService confirmingService,
            OrderItemToItemDtoConversionService oi2ItemDtoConversionService,
            ReportItemToItemDtoConverterService riToItemConversionService,
            ShippingService shippingService,
            InvoicingService invoicingService,
            CatalogDeliveryMethodRepository deliveryMethodRepo,
            AgreeingService agreeingService) {
        this.cpRepo = cpRepo;
        this.cRepo = cRepo;
        this.orderingService = orderingService;
        this.confirmingService = confirmingService;
        this.oi2ItemDtoConversionService = oi2ItemDtoConversionService;
        this.riToItemConversionService = riToItemConversionService;
        this.shippingService = shippingService;
        this.invoicingService = invoicingService;
        this.deliveryMethodRepo = deliveryMethodRepo;
        this.agreeingService = agreeingService;
    }

    public TestDataCreator() {}

    public void run() {
        createTestData();
    }

    public void createTestData() {
        createProducts();
        createCustomers();
        createDeliveryMethods();

        createYvonnesOrders();
        createNaidasOrders();
    }

    private void createDeliveryMethods() {
        deliveryMethodRepo.save(new CatalogDeliveryMethod(UPS));
        deliveryMethodRepo.save(new CatalogDeliveryMethod(DHL));
        deliveryMethodRepo.flush();
    }

    private void createNaidasOrders() {
        LocalDate dt = LocalDate.now();

        orderingService.order(B21);
        Order b22 = orderingService.order(B22);
        OrderConfirmation ab22 = createAB22(dt, b22);

        orderingService.cancelReport(ab22.getDocumentNumber());

    }

    private OrderConfirmation createAB22(LocalDate dt, Order b22) {
        ConfirmParameter confirmParameter = new ConfirmParameter(
                b22.getOrderNumber(),
                "AB22",
                dt.plusDays(5),
                null,
                YVONNE.getShippingAddress(),
                YVONNE.getInvoiceAddress(),
                oi2ItemDtoConversionService.convert(b22));
        confirmParameter.setPaymentConditions("5 % Skonto, wenn innerhalb 5 Tagen");
        OrderConfirmation ab22 = confirmingService.confirm(confirmParameter);
        return ab22;
    }

    private void createYvonnesOrders() {

        orderingService.order(B11);
        orderingService.order(B12);
        orderingService.order(B13);
        orderingService.order(B15);

        OrderConfirmation ab11 = confirmingService.confirm(AB11);
        confirmingService.confirm(AB13);
        OrderConfirmation ab15 = confirmingService.confirm(AB15);

        ab11 = agreeingService.agree(ab11.getDocumentNumber(), "AU11");
        ab15 = agreeingService.agree(ab15.getDocumentNumber(), "AU15");

        List<ItemDto> itemsFromAu11 = convertReport(ab11);
        List<ItemDto> itemsFromAu15 = convertReport(ab15);

        DeliveryNotes l11 = createL11(itemsFromAu11);
        DeliveryNotes l12 = createL12(itemsFromAu11);
        createL13(itemsFromAu11);
        createL14(itemsFromAu11);
        createL15(itemsFromAu11, itemsFromAu15);

        createR11(convertReport(l11), convertReport(l12));
    }

    private void createR11(List<ItemDto> l11, List<ItemDto> l12) {
        ItemDto shippingCosts = new ItemDto();
        shippingCosts.setPriceNet(BigDecimal.valueOf(11d));
        shippingCosts.setProductType(ProductType.SHIPPING);
        InvoicingParameter invoicingParameter = new InvoicingParameter("R11", LocalDate.now(), Arrays.asList(
                extract(l11, AMY.getProductNumber(), 2),
                extract(l11, MILADKA.getProductNumber(), 2),
                extract(l12, AMY.getProductNumber(), 3),
                extract(l12, MILADKA.getProductNumber(), 3),
                shippingCosts));
        invoicingService.invoice(invoicingParameter);
    }

    private void createL15(List<ItemDto> itemsFromAu11, List<ItemDto> itemsFromAu15) {
        DeliverParameter deliverParameter = new DeliverParameter(
                "L15",
                LocalDate.now(),
                Arrays.asList(
                        extract(itemsFromAu11, PAUL.getProductNumber(), 15),
                        extract(itemsFromAu15, PAUL.getProductNumber(), 8)));
        deliverParameter.setTrackNumber("trackNumber15");
        deliverParameter.setPackageNumber("packageNumber15");
        deliverParameter.setShipment(BigDecimal.ZERO);
        deliverParameter.setIgnoreContradictoryExpectedDeliveryDates(true);
        shippingService.ship(deliverParameter);
    }

    private void createL14(List<ItemDto> itemsFromAu11) {
        DeliverParameter deliverParameter = new DeliverParameter(
                "L14",
                LocalDate.now(),
                Arrays.asList(extract(itemsFromAu11, PAUL.getProductNumber(), 5)));
        deliverParameter.setTrackNumber("trackNumber14");
        deliverParameter.setPackageNumber("packageNumber14");
        deliverParameter.setShipment(BigDecimal.ZERO);
        deliverParameter.setIgnoreContradictoryExpectedDeliveryDates(true);
        shippingService.ship(deliverParameter);
    }

    private void createL13(List<ItemDto> itemsFromAu11) {
        DeliverParameter deliverParameter = new DeliverParameter(
                "L13",
                LocalDate.now(),
                Arrays.asList(
                        extract(itemsFromAu11, SALOME.getProductNumber(), 1),
                        extract(itemsFromAu11, JUREK.getProductNumber(), 5)));
        deliverParameter.setTrackNumber("trackNumber13");
        deliverParameter.setPackageNumber("packageNumber13");
        deliverParameter.setIgnoreContradictoryExpectedDeliveryDates(true);
        deliverParameter.setShipment(BigDecimal.ONE);
        shippingService.ship(deliverParameter);
    }

    private DeliveryNotes createL12(List<ItemDto> itemsFromAu11) {
        DeliverParameter deliverParameter = new DeliverParameter(
                "L12",
                LocalDate.now(),
                Arrays.asList(
                        extract(itemsFromAu11, AMY.getProductNumber(), 3),
                        extract(itemsFromAu11, MILADKA.getProductNumber(), 3)));
        deliverParameter.setTrackNumber("trackNumber12");
        deliverParameter.setPackageNumber("packageNumber12");
        deliverParameter.setShipment(BigDecimal.ONE);
        deliverParameter.setIgnoreContradictoryExpectedDeliveryDates(true);
        DeliveryNotes l12 = shippingService.ship(deliverParameter);
        return l12;
    }

    private DeliveryNotes createL11(List<ItemDto> itemsFromAu11) {
        DeliverParameter deliverParameter = new DeliverParameter(
                "L11",
                LocalDate.now(),
                Arrays.asList(
                        extract(itemsFromAu11, AMY.getProductNumber(), 2),
                        extract(itemsFromAu11, MILADKA.getProductNumber(), 2)));
        deliverParameter.setTrackNumber("trackNumber");
        deliverParameter.setPackageNumber("packageNumber");
        deliverParameter.setShipment(BigDecimal.TEN);
        deliverParameter.setIgnoreContradictoryExpectedDeliveryDates(true);
        DeliveryNotes l11 = shippingService.ship(deliverParameter);
        return l11;
    }

    private ItemDto extract(List<ItemDto> itemDtos, String productNumber, int i) {
        for (ItemDto item : itemDtos) {
            if (item.getProduct().equals(productNumber)) {
                item.setQuantityLeft(i);
                return item;
            }
        }
        return null;
    }

    private List<CatalogProduct> createProducts() {
        Set<CatalogProduct> products = new HashSet<CatalogProduct>();
        products.add(AMY);
        products.add(JUREK);
        products.add(MILADKA);
        products.add(PAUL);
        products.add(SALOME);
        return cpRepo.save(products);
    }

    private void createCustomers() {
        Set<Customer> customers = new HashSet<Customer>();
        customers.add(EDWARD);
        customers.add(JEROME);
        customers.add(NAIDA);
        customers.add(WYOMING);
        customers.add(YVONNE);
        cRepo.save(customers);
        cRepo.flush();
    }

    public List<ItemDto> convertReport(Report report) {
        List<ItemDto> ris = new ArrayList<ItemDto>();
        for (ReportItem ri : report.getItems()) {
            ris.add(riToItemConversionService.createOverdue(ri));
        }
        return ris;
    }

    public boolean isTestDataPersisted() {
        Customer c = cRepo.findByCustomerNumber(EDWARD.getCustomerNumber());
        return c != null;
    }

}
