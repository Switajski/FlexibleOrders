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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.CatalogDeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.CatalogDeliveryMethodRepository;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.service.api.AgreementService;
import de.switajski.priebes.flexibleorders.service.api.DeliveryService;
import de.switajski.priebes.flexibleorders.service.api.InvoicingParameter;
import de.switajski.priebes.flexibleorders.service.api.InvoicingService;
import de.switajski.priebes.flexibleorders.service.api.OrderConfirmationService;
import de.switajski.priebes.flexibleorders.service.api.OrderService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.testhelper.AbstractSpringContextTest;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

/**
 * Creates test data in order to ease GUI Testing.
 * 
 * @author Marek Switajski
 * 
 */
@Transactional
public class TestDataCreator extends AbstractSpringContextTest {

    @Autowired
    private CatalogProductRepository cpRepo;

    @Autowired
    private CustomerRepository cRepo;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderConfirmationService orderConfirmationService;

    @Autowired
    private ItemDtoConverterService converterService;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private InvoicingService invoicingService;

    @Autowired
    private CatalogDeliveryMethodRepository deliveryMethodRepo;

    @Autowired
    private AgreementService agreementService;

    // @Ignore("This test is to initialize test data for GUI testing")
//    @Test
//    @Rollback(false)
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
        DateTime dt = new DateTime();

        orderService.order(B21);
        Order b22 = orderService.order(B22);
        OrderConfirmation ab22 = createAB22(dt, b22);

        orderService.cancelReport(ab22.getDocumentNumber());

    }

    private OrderConfirmation createAB22(DateTime dt, Order b22) {
        ConfirmParameter confirmParameter = new ConfirmParameter(
                b22.getOrderNumber(),
                "AB22",
                dt.plusDays(5).toLocalDate(),
                null,
                YVONNE.getShippingAddress(),
                YVONNE.getInvoiceAddress(),
                converterService.convert(b22));
        confirmParameter.paymentConditions = "5 % Skonto, wenn innerhalb 5 Tagen";
		OrderConfirmation ab22 = orderConfirmationService.confirm(confirmParameter);
        return ab22;
    }

    private void createYvonnesOrders() {

        orderService.order(B11);
        orderService.order(B12);
        orderService.order(B13);
        orderService.order(B15);

        OrderConfirmation ab11 = orderConfirmationService.confirm(AB11);
        orderConfirmationService.confirm(AB13);
        OrderConfirmation ab15 = orderConfirmationService.confirm(AB15);

        ab11 = agreementService.agree(ab11.getDocumentNumber(), "AU11");
        ab15 = agreementService.agree(ab15.getDocumentNumber(), "AU15");

        List<ItemDto> itemsFromAu11 = converterService.convertReport(ab11);
        List<ItemDto> itemsFromAu15 = converterService.convertReport(ab15);

        DeliveryNotes l11 = createL11(itemsFromAu11);
        DeliveryNotes l12 = createL12(itemsFromAu11);
        createL13(itemsFromAu11);
        createL14(itemsFromAu11);
        createL15(itemsFromAu11, itemsFromAu15);

        List<ItemDto> l11AndL12 = converterService.convertReport(l11);
        l11AndL12.addAll(converterService.convertReport(l12));

        createR11(l11AndL12);
    }

    private void createR11(List<ItemDto> l11AndL12) {
        ItemDto shippingCosts = new ItemDto();
        shippingCosts.priceNet = BigDecimal.valueOf(11d);
        shippingCosts.productType = ProductType.SHIPPING;
        InvoicingParameter invoicingParameter = new InvoicingParameter("R11", new Date(), Arrays.asList(
                extract(l11AndL12, AMY.getProductNumber(), 5),
                extract(l11AndL12, MILADKA.getProductNumber(), 5),
                shippingCosts));
        invoicingService.invoice(
                invoicingParameter);
    }

    private void createL15(List<ItemDto> itemsFromAu11, List<ItemDto> itemsFromAu15) {
        DeliverParameter deliverParameter = new DeliverParameter(
                "L15",
                new Date(),
                Arrays.asList(
                        extract(itemsFromAu11, PAUL.getProductNumber(), 15),
                        extract(itemsFromAu15, PAUL.getProductNumber(), 8)));
        deliverParameter.trackNumber = "trackNumber15";
        deliverParameter.packageNumber = "packageNumber15"; 
        deliverParameter.shipment = new Amount(BigDecimal.ZERO);
        deliveryService.deliver(deliverParameter);
    }

    private void createL14(List<ItemDto> itemsFromAu11) {
        DeliverParameter deliverParameter = new DeliverParameter(
                "L14",
                new Date(),
                Arrays.asList(extract(itemsFromAu11, PAUL.getProductNumber(), 5)));
        deliverParameter.trackNumber = "trackNumber14";
        deliverParameter.packageNumber = "packageNumber14";
        deliverParameter.shipment = new Amount(BigDecimal.ZERO);
        deliveryService.deliver(deliverParameter);
    }

    private void createL13(List<ItemDto> itemsFromAu11) {
        DeliverParameter deliverParameter = new DeliverParameter(
                "L13",
                new Date(),
                Arrays.asList(
                        extract(itemsFromAu11, SALOME.getProductNumber(), 1),
                        extract(itemsFromAu11, JUREK.getProductNumber(), 5)));
        deliverParameter.trackNumber = "trackNumber13";
        deliverParameter.packageNumber = "packageNumber13";
        deliverParameter.shipment = new Amount(BigDecimal.ONE);
        deliveryService.deliver(deliverParameter);
    }

    private DeliveryNotes createL12(List<ItemDto> itemsFromAu11) {
        DeliverParameter deliverParameter = new DeliverParameter(
                "L12",
                new Date(),
                Arrays.asList(
                        extract(itemsFromAu11, AMY.getProductNumber(), 3),
                        extract(itemsFromAu11, MILADKA.getProductNumber(), 3)));
        deliverParameter.trackNumber = "trackNumber12";
        deliverParameter.packageNumber = "packageNumber12";
        deliverParameter.shipment = new Amount(BigDecimal.ONE); 
        DeliveryNotes l12 = deliveryService.deliver(deliverParameter);
        return l12;
    }

    private DeliveryNotes createL11(List<ItemDto> itemsFromAu11) {
        DeliverParameter deliverParameter = new DeliverParameter(
                "L11",
                new Date(),
                Arrays.asList(
                        extract(itemsFromAu11, AMY.getProductNumber(), 2),
                        extract(itemsFromAu11, MILADKA.getProductNumber(), 2)));
        deliverParameter.trackNumber = "trackNumber";
        deliverParameter.packageNumber = "packageNumber";
        deliverParameter.shipment = new Amount(BigDecimal.TEN);
        DeliveryNotes l11 = deliveryService.deliver(
                deliverParameter);
        return l11;
    }

    private ItemDto extract(List<ItemDto> itemDtos, String productNumber, int i) {
        for (ItemDto item : itemDtos) {
            if (item.product.equals(productNumber)) {
                item.quantityLeft = i;
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

}
