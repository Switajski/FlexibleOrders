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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.CatalogDeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Customer;
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
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConversionService;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
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

    private ReportItemToItemDtoConversionService riToItemConversionService;

    private ShippingService shippingService;

    private InvoicingService invoicingService;

    private CatalogDeliveryMethodRepository deliveryMethodRepo;

    private AgreeingService agreeingService;

    public TestDataCreator(
            CatalogProductRepository cpRepo,
            CustomerRepository cRepo,
            TransitionsService orderingService,
            ConfirmingService confirmingService,
            ReportItemToItemDtoConversionService riToItemConversionService,
            ShippingService shippingService,
            InvoicingService invoicingService,
            CatalogDeliveryMethodRepository deliveryMethodRepo,
            AgreeingService agreeingService) {
        this.cpRepo = cpRepo;
        this.cRepo = cRepo;
        this.orderingService = orderingService;
        this.confirmingService = confirmingService;
        this.riToItemConversionService = riToItemConversionService;
        this.shippingService = shippingService;
        this.invoicingService = invoicingService;
        this.deliveryMethodRepo = deliveryMethodRepo;
        this.agreeingService = agreeingService;
    }

    public TestDataCreator() {}

    public void run() throws Exception {
        createTestData();
    }

    public void createTestData() throws Exception {
        createProducts();
        createCustomers();
        createDeliveryMethods();

        createYvonnesOrders();
        createNaidasOrders();
        createEdwardsOrders();
    }

    private void createDeliveryMethods() {
        deliveryMethodRepo.save(new CatalogDeliveryMethod(UPS));
        deliveryMethodRepo.save(new CatalogDeliveryMethod(DHL));
        deliveryMethodRepo.flush();
    }

    private void createNaidasOrders() {
        LocalDate dt = LocalDate.now();

        orderingService.order(B21);
        Set<ItemDto> b22 = orderingService.order(B22);
        Set<ItemDto> ab22 = createAB22(dt, b22);

        orderingService.cancelReport(ab22.iterator().next().getDocumentNumber());

    }

    private void createEdwardsOrders() {
        Set<ItemDto> b31 = orderingService.order(TestDataFixture.B31);
        createAB31(LocalDate.now(), b31);
    }

    private Set<ItemDto> createAB31(LocalDate now, Set<ItemDto> b31) {
        ConfirmParameter confirmParameter = new ConfirmParameter(
                b31.iterator().next().getOrderNumber(),
                "AB31",
                now.plusDays(5),
                null,
                EDWARD.getShippingAddress(),
                EDWARD.getInvoiceAddress(),
                new ArrayList<ItemDto>(b31));
        confirmParameter.setPaymentConditions("5 % Skonto, wenn innerhalb 5 Tagen");
        Set<ItemDto> ab31 = confirmingService.confirm(confirmParameter);
        agreeingService.agree(ab31.iterator().next().getDocumentNumber(), "AU31");
        return ab31;
    }

    private Set<ItemDto> createAB22(LocalDate dt, Set<ItemDto> b22) {
        ConfirmParameter confirmParameter = new ConfirmParameter(
                b22.iterator().next().getOrderNumber(),
                "AB22",
                dt.plusDays(5),
                null,
                YVONNE.getShippingAddress(),
                YVONNE.getInvoiceAddress(),
                new ArrayList<ItemDto>(b22));
        confirmParameter.setPaymentConditions("5 % Skonto, wenn innerhalb 5 Tagen");
        Set<ItemDto> ab22 = confirmingService.confirm(confirmParameter);
        return ab22;
    }

    private void createYvonnesOrders() throws Exception {

        orderingService.order(B11);
        orderingService.order(B12);
        orderingService.order(B13);
        orderingService.order(B15);

        Set<ItemDto> ab11 = confirmingService.confirm(AB11);
        Set<ItemDto> ab13 = confirmingService.confirm(AB13);
        ConfirmParameter ab15Param = AB15;
        ab15Param.setInvoiceAddress(AddressBuilder.buildWithGeneratedAttributes(87));
        Set<ItemDto> ab15 = confirmingService.confirm(ab15Param);

        agreeingService.agree(ab11.iterator().next().getDocumentNumber(), "AU11");
        agreeingService.agree(ab13.iterator().next().getDocumentNumber(), "AU13");
        agreeingService.agree(ab15.iterator().next().getDocumentNumber(), "AU15");

        Set<ItemDto> l11 = createL11(ab11);
        Set<ItemDto> l12 = createL12(ab11);
        createL13(ab11);
        createL14(ab11);
        createL15(ab11, ab15);

        createR11(l11, l12);
    }

    private void createR11(Collection<ItemDto> l11, Collection<ItemDto> l12) throws Exception {
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

    private void createL15(Collection<ItemDto> itemsFromAu11, Collection<ItemDto> itemsFromAu15) throws Exception {
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

    private void createL14(Collection<ItemDto> itemsFromAu11) throws Exception {
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

    private Set<ItemDto> createL13(Collection<ItemDto> itemsFromAu11) throws Exception {
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
        return shippingService.ship(deliverParameter);
    }

    private Set<ItemDto> createL12(Collection<ItemDto> itemsFromAu11) throws Exception {
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
        return shippingService.ship(deliverParameter);
    }

    private Set<ItemDto> createL11(Collection<ItemDto> itemsFromAu11) throws Exception {
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
        return shippingService.ship(deliverParameter);
    }

    private ItemDto extract(Collection<ItemDto> itemDtos, String productNumber, int i) {
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

    public List<ItemDto> convertToItemDtos(Report report) {
        List<ItemDto> ris = new ArrayList<ItemDto>();
        for (ReportItem ri : report.getItems()) {
            ris.add(riToItemConversionService.createMissing(ri));
        }
        return ris;
    }

    public boolean isTestDataPersisted() {
        Customer c = cRepo.findByCustomerNumber(EDWARD.getCustomerNumber());
        return c != null;
    }

}
