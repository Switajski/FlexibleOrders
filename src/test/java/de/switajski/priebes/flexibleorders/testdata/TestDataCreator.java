package de.switajski.priebes.flexibleorders.testdata;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
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
import de.switajski.priebes.flexibleorders.service.api.OrderService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.parameter.ConfirmParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;
import de.switajski.priebes.flexibleorders.service.process.parameter.OrderParameter;
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
	@Test
	@Rollback(false)
	public void createTestData() {
		createProducts();
		createCustomers();
		createDeliveryMethods();

		createYvonnesOrders();
		createNaidasOrders();
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void createDeliveryMethods() {
		deliveryMethodRepo.save(new CatalogDeliveryMethod(UPS));
		deliveryMethodRepo.save(new CatalogDeliveryMethod(DHL));
	}

	private void createNaidasOrders() {
		DateTime dt = new DateTime();

		createB21(dt);
		Order b22 = createB22(dt);
		OrderConfirmation ab22 = createAB22(dt, b22);

		orderService.cancelReport(ab22.getDocumentNumber());

	}

    private OrderConfirmation createAB22(DateTime dt, Order b22) {
        OrderConfirmation ab22 = orderService.confirm(new ConfirmParameter(
		        b22.getOrderNumber(),
		        "AB22",
		        dt.plusDays(5).toLocalDate(),
		        DHL.getExternalId(),
		        YVONNE.getShippingAddress(),
		        YVONNE.getInvoiceAddress(),
		        converterService.convert(b22)));
        return ab22;
    }

    private Order createB22(DateTime dt) {
        Order b22 = orderService.order(new OrderParameter(
		        NAIDA.getCustomerNumber(),
		        "B22",
		        dt.toDate(), 
		        converterService.convertOrderItems(
		                B22.createOrderItems())));
        return b22;
    }

    private void createB21(DateTime dt) {
        orderService.order(new OrderParameter(
		        NAIDA.getCustomerNumber(),
		        "B21",
		        dt.toDate(),
		        converterService.convertOrderItems(B21.createOrderItems())));
    }

	private void createYvonnesOrders() {
		DateTime dt = new DateTime();
		
		Order b11 = createB11(dt);
		Order b12 = createB12(dt);
		Order b13 = createB13(dt);
		Order b15 = createB15(dt);

		List<ItemDto> b11Andb12 = new ArrayList<ItemDto>();
		b11Andb12.addAll(converterService.convert(b11));
		b11Andb12.addAll(converterService.convert(b12));

		OrderConfirmation ab11 = createAB11(dt, b11, b11Andb12);

		createAB13(dt, b11, b13);
		OrderConfirmation ab15 = createAB15(dt, b11, b15);

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
        invoicingService.invoice(
				new InvoicingParameter("R11", "5 % Skonto, wenn innerhalb 5 Tagen", new Date(), Arrays.asList(
						extract(l11AndL12, AMY.getProductNumber(), 5),
						extract(l11AndL12, MILADKA.getProductNumber(), 5),
						shippingCosts), 
						"billing"));
    }

    private void createL15(List<ItemDto> itemsFromAu11, List<ItemDto> itemsFromAu15) {
        deliveryService.deliver(new DeliverParameter(
		        "L15",
		        "trackNumber15",
		        "packageNumber15",
		        new Amount(BigDecimal.ZERO),
		        new Date(),
		        Arrays.asList(
		                extract(itemsFromAu11, PAUL.getProductNumber(), 15), 
		                extract(itemsFromAu15, PAUL.getProductNumber(), 8))));
    }

    private void createL14(List<ItemDto> itemsFromAu11) {
        deliveryService.deliver(new DeliverParameter(
		        "L14",
		        "trackNumber14",
		        "packageNumber14",
		        new Amount(BigDecimal.ZERO),
		        new Date(),
		        Arrays.asList(extract(itemsFromAu11, PAUL.getProductNumber(), 5))));
    }

    private void createL13(List<ItemDto> itemsFromAu11) {
        deliveryService.deliver(new DeliverParameter(
		        "L13",
		        "trackNumber13",
		        "packageNumber13",
		        new Amount(BigDecimal.ONE),
		        new Date(),
		        Arrays.asList(
		                extract(itemsFromAu11, SALOME.getProductNumber(), 1),
		                extract(itemsFromAu11, JUREK.getProductNumber(), 5))));
    }

    private DeliveryNotes createL12(List<ItemDto> itemsFromAu11) {
        DeliveryNotes l12 = deliveryService.deliver(new DeliverParameter(
		        "L12",
		        "trackNumber12",
		        "packageNumber12",
		        new Amount(BigDecimal.ONE),
		        new Date(),
		        Arrays.asList(
		                extract(itemsFromAu11, AMY.getProductNumber(), 3),
		                extract(itemsFromAu11, MILADKA.getProductNumber(), 3))));
        return l12;
    }

    private DeliveryNotes createL11(List<ItemDto> itemsFromAu11) {
        DeliveryNotes l11 = deliveryService.deliver(
				new DeliverParameter(
						"L11",
						"trackNumber",
						"packageNumber",
						new Amount(BigDecimal.TEN),
						new Date(),
						Arrays.asList(
								extract(itemsFromAu11, AMY.getProductNumber(), 2),
								extract(itemsFromAu11, MILADKA.getProductNumber(), 2))));
        return l11;
    }

    private OrderConfirmation createAB15(DateTime dt, Order b11, Order b15) {
        ConfirmParameter cpAB15 = new ConfirmParameter();
        cpAB15.orderNumber = b11.getOrderNumber();
        cpAB15.confirmNumber = "AB15";
        cpAB15.expectedDelivery = dt.plusDays(10).toLocalDate();
        cpAB15.deliveryMethodNo = DHL.getExternalId();
        cpAB15.shippingAddress = YVONNE.getShippingAddress();
        cpAB15.invoiceAddress = YVONNE.getInvoiceAddress();
        cpAB15.orderItems = converterService.convert(b15);
        cpAB15.customerDetails = YVONNE.getDetails();
        return orderService.confirm(cpAB15);
    }

    private void createAB13(DateTime dt, Order b11, Order b13) {
        ConfirmParameter cpAB13 = new ConfirmParameter();
		cpAB13.orderNumber = b11.getOrderNumber();
		cpAB13.confirmNumber = "AB13";
		cpAB13.expectedDelivery = dt.plusDays(2).toLocalDate();
		cpAB13.deliveryMethodNo = DHL.getExternalId();
		cpAB13.shippingAddress = YVONNE.getShippingAddress();
		cpAB13.invoiceAddress = YVONNE.getInvoiceAddress();
		cpAB13.orderItems = converterService.convert(b13);
		cpAB13.customerDetails = YVONNE.getDetails();
		orderService.confirm(cpAB13);
    }

    private OrderConfirmation createAB11(DateTime dt, Order b11, List<ItemDto> b11Andb12) {
        ConfirmParameter cpAB11 = new ConfirmParameter();
		cpAB11.orderNumber = b11.getOrderNumber();
		cpAB11.confirmNumber = "AB11";
		cpAB11.expectedDelivery = dt.plusDays(10).toLocalDate();
		cpAB11.deliveryMethodNo = DHL.getExternalId();
		cpAB11.shippingAddress = YVONNE.getShippingAddress();
		cpAB11.invoiceAddress = YVONNE.getInvoiceAddress();
		cpAB11.orderItems = b11Andb12;
		cpAB11.customerDetails = YVONNE.getDetails();
		OrderConfirmation ab11 = orderService.confirm(cpAB11);
        return ab11;
    }

    private Order createB15(DateTime dt) {
        OrderParameter opB15 = new OrderParameter();
        opB15.customerNumber = YVONNE.getCustomerNumber();
        opB15.orderNumber = "B15";
        opB15.reportItems = converterService.convertOrderItems(
                        B15.createOrderItems());
        opB15.expectedDelivery = dt.plusDays(2).toLocalDate();
        Order b15 = orderService.order(opB15);
        return b15;
    }

    private Order createB13(DateTime dt) {
        OrderParameter opB13 = new OrderParameter();
		opB13.customerNumber = YVONNE.getCustomerNumber();
		opB13.orderNumber = "B13";
		opB13.reportItems = converterService.convertOrderItems(
						B13.createOrderItems());
		opB13.expectedDelivery = dt.plusDays(2).toLocalDate();
		Order b13 = orderService.order(opB13);
        return b13;
    }

    private Order createB12(DateTime dt) {
        OrderParameter opB12 = new OrderParameter();
		opB12.customerNumber = YVONNE.getCustomerNumber();
		opB12.orderNumber= "B12";
		opB12.reportItems = converterService.convertOrderItems(
				B12.createOrderItems());
		opB12.expectedDelivery = dt.plusDays(2).toLocalDate();
		Order b12 = orderService.order(opB12);
        return b12;
    }

    private Order createB11(DateTime dt) {
        OrderParameter opB11 = new OrderParameter();
		opB11.customerNumber = YVONNE.getCustomerNumber();
		opB11.orderNumber = "B11";
		opB11.reportItems = converterService.convertOrderItems(B11.createOrderItems());
		opB11.expectedDelivery = dt.plusDays(2).toLocalDate();
		Order b11 = orderService.order(opB11);
        return b11;
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

	@Before
	public void createCustomers() {
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
