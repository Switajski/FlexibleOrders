package de.switajski.priebes.flexibleorders.testdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import de.switajski.priebes.flexibleorders.domain.CatalogDeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
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
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.DeliveryMethodBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

/**
 * Creates test data in order to ease GUI Testing.
 * 
 * @author Marek Switajski
 * 
 */
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
	@Rollback(false)
	@Test
	public void createTestData() {
		createProducts();
		createCustomers();
		createDeliveryMethods();

		createYvonnesOrders();
		createNaidasOrders();
	}

	private void createDeliveryMethods() {
		deliveryMethodRepo.save(new CatalogDeliveryMethod(TestData.UPS));
		deliveryMethodRepo.save(new CatalogDeliveryMethod(TestData.DHL));
		deliveryMethodRepo.flush();
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
		        TestData.DHL.getId(),
		        TestData.YVONNE.getShippingAddress(),
		        TestData.YVONNE.getInvoiceAddress(),
		        converterService.convert(b22)));
        return ab22;
    }

    private Order createB22(DateTime dt) {
        Order b22 = orderService.order(new OrderParameter(
		        TestData.NAIDA.getCustomerNumber(),
		        "B22",
		        dt.toDate(), 
		        converterService.convertOrderItems(
		                TestData.ORDERITEMS_OF_B22)));
        return b22;
    }

    private void createB21(DateTime dt) {
        orderService.order(new OrderParameter(
		        TestData.NAIDA.getCustomerNumber(),
		        "B21",
		        dt.toDate(),
		        converterService.convertOrderItems(TestData.ORDERITEMS_OF_B21)));
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
						extract(l11AndL12, TestData.AMY.getProductNumber(), 5),
						extract(l11AndL12, TestData.MILADKA.getProductNumber(), 5),
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
		                extract(itemsFromAu11, TestData.PAUL.getProductNumber(), 15), 
		                extract(itemsFromAu15, TestData.PAUL.getProductNumber(), 8))));
    }

    private void createL14(List<ItemDto> itemsFromAu11) {
        deliveryService.deliver(new DeliverParameter(
		        "L14",
		        "trackNumber14",
		        "packageNumber14",
		        new Amount(BigDecimal.ZERO),
		        new Date(),
		        Arrays.asList(extract(itemsFromAu11, TestData.PAUL.getProductNumber(), 5))));
    }

    private void createL13(List<ItemDto> itemsFromAu11) {
        deliveryService.deliver(new DeliverParameter(
		        "L13",
		        "trackNumber13",
		        "packageNumber13",
		        new Amount(BigDecimal.ONE),
		        new Date(),
		        Arrays.asList(
		                extract(itemsFromAu11, TestData.SALOME.getProductNumber(), 1),
		                extract(itemsFromAu11, TestData.JUREK.getProductNumber(), 5))));
    }

    private DeliveryNotes createL12(List<ItemDto> itemsFromAu11) {
        DeliveryNotes l12 = deliveryService.deliver(new DeliverParameter(
		        "L12",
		        "trackNumber12",
		        "packageNumber12",
		        new Amount(BigDecimal.ONE),
		        new Date(),
		        Arrays.asList(
		                extract(itemsFromAu11, TestData.AMY.getProductNumber(), 3),
		                extract(itemsFromAu11, TestData.MILADKA.getProductNumber(), 3))));
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
								extract(itemsFromAu11, TestData.AMY.getProductNumber(), 2),
								extract(itemsFromAu11, TestData.MILADKA.getProductNumber(), 2))));
        return l11;
    }

    private OrderConfirmation createAB15(DateTime dt, Order b11, Order b15) {
        ConfirmParameter cpAB15 = new ConfirmParameter();
        cpAB15.orderNumber = b11.getOrderNumber();
        cpAB15.confirmNumber = "AB15";
        cpAB15.expectedDelivery = dt.plusDays(10).toLocalDate();
        cpAB15.deliveryMethodNo = TestData.DHL.getId();
        cpAB15.shippingAddress = TestData.YVONNE.getShippingAddress();
        cpAB15.invoiceAddress = TestData.YVONNE.getInvoiceAddress();
        cpAB15.orderItems = converterService.convert(b15);
        cpAB15.customerDetails = TestData.YVONNE.getDetails();
        return orderService.confirm(cpAB15);
    }

    private void createAB13(DateTime dt, Order b11, Order b13) {
        ConfirmParameter cpAB13 = new ConfirmParameter();
		cpAB13.orderNumber = b11.getOrderNumber();
		cpAB13.confirmNumber = "AB13";
		cpAB13.expectedDelivery = dt.plusDays(2).toLocalDate();
		cpAB13.deliveryMethodNo = TestData.DHL.getId();
		cpAB13.shippingAddress = TestData.YVONNE.getShippingAddress();
		cpAB13.invoiceAddress = TestData.YVONNE.getInvoiceAddress();
		cpAB13.orderItems = converterService.convert(b13);
		cpAB13.customerDetails = TestData.YVONNE.getDetails();
		orderService.confirm(cpAB13);
    }

    private OrderConfirmation createAB11(DateTime dt, Order b11, List<ItemDto> b11Andb12) {
        ConfirmParameter cpAB11 = new ConfirmParameter();
		cpAB11.orderNumber = b11.getOrderNumber();
		cpAB11.confirmNumber = "AB11";
		cpAB11.expectedDelivery = dt.plusDays(10).toLocalDate();
		cpAB11.deliveryMethodNo = TestData.DHL.getId();
		cpAB11.shippingAddress = TestData.YVONNE.getShippingAddress();
		cpAB11.invoiceAddress = TestData.YVONNE.getInvoiceAddress();
		cpAB11.orderItems = b11Andb12;
		cpAB11.customerDetails = TestData.YVONNE.getDetails();
		OrderConfirmation ab11 = orderService.confirm(cpAB11);
        return ab11;
    }

    private Order createB15(DateTime dt) {
        OrderParameter opB15 = new OrderParameter();
        opB15.customerNumber = TestData.YVONNE.getCustomerNumber();
        opB15.orderNumber = "B15";
        opB15.reportItems = converterService.convertOrderItems(
                        TestData.ORDERITEMS_OF_B15);
        opB15.expectedDelivery = dt.plusDays(2).toLocalDate();
        Order b15 = orderService.order(opB15);
        return b15;
    }

    private Order createB13(DateTime dt) {
        OrderParameter opB13 = new OrderParameter();
		opB13.customerNumber = TestData.YVONNE.getCustomerNumber();
		opB13.orderNumber = "B13";
		opB13.reportItems = converterService.convertOrderItems(
						TestData.ORDERITEMS_OF_B13);
		opB13.expectedDelivery = dt.plusDays(2).toLocalDate();
		Order b13 = orderService.order(opB13);
        return b13;
    }

    private Order createB12(DateTime dt) {
        OrderParameter opB12 = new OrderParameter();
		opB12.customerNumber = TestData.YVONNE.getCustomerNumber();
		opB12.orderNumber= "B12";
		opB12.reportItems = converterService.convertOrderItems(
				TestData.ORDERITEMS_OF_B12);
		opB12.expectedDelivery = dt.plusDays(2).toLocalDate();
		Order b12 = orderService.order(opB12);
        return b12;
    }

    private Order createB11(DateTime dt) {
        OrderParameter opB11 = new OrderParameter();
		opB11.customerNumber = TestData.YVONNE.getCustomerNumber();
		opB11.orderNumber = "B11";
		opB11.reportItems = converterService.convertOrderItems(
						TestData.ORDERITEMS_OF_B11);
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
		products.add(TestData.AMY);
		products.add(TestData.JUREK);
		products.add(TestData.MILADKA);
		products.add(TestData.PAUL);
		products.add(TestData.SALOME);
		return cpRepo.save(products);
	}

	@Before
	public void createCustomers() {
		Set<Customer> customers = new HashSet<Customer>();
		customers.add(TestData.EDWARD);
		customers.add(TestData.JEROME);
		customers.add(TestData.NAIDA);
		customers.add(TestData.WYOMING);
		customers.add(TestData.YVONNE);
		cRepo.save(customers);
		cRepo.flush();
	}

	private static class TestData {
		static final Customer YVONNE = new CustomerBuilder().yvonne().build();

		static final Customer WYOMING = new CustomerBuilder().wyoming().build();

		static final Customer NAIDA = new CustomerBuilder().naida().build();

		static final Customer JEROME = new CustomerBuilder().jerome().build();

		static final Customer EDWARD = new CustomerBuilder().edward().build();

		static final CatalogProduct SALOME = new CatalogProductBuilder()
				.salome().build();

		static final CatalogProduct PAUL = new CatalogProductBuilder()
				.paul().build();

		static final CatalogProduct MILADKA = new CatalogProductBuilder()
				.miladka().build();

		static final CatalogProduct JUREK = new CatalogProductBuilder()
				.jurek().build();

		static final CatalogProduct AMY = new CatalogProductBuilder()
				.amy()
				.build();

		static final DeliveryMethod UPS = new DeliveryMethodBuilder().ups().build();

		static final DeliveryMethod DHL = new DeliveryMethodBuilder().dhl().build();

		static final List<OrderItem> ORDERITEMS_OF_B11 = Arrays
				.<OrderItem> asList(
						new OrderItemBuilder()
								.setProduct(AMY.toProduct())
								.setOrderedQuantity(10)
								.setNegotiatedPriceNet(
										AMY.getRecommendedPriceNet())
								.build(),
						new OrderItemBuilder()
								.setProduct(MILADKA.toProduct())
								.setOrderedQuantity(15)
								.setNegotiatedPriceNet(
										MILADKA.getRecommendedPriceNet())
								.build(),
						new OrderItemBuilder()
								.setProduct(PAUL.toProduct())
								.setOrderedQuantity(30)
								.setNegotiatedPriceNet(
										PAUL.getRecommendedPriceNet())
								.build());

		static final List<OrderItem> ORDERITEMS_OF_B12 =
				Arrays.<OrderItem> asList(
						new OrderItemBuilder()
								.setProduct(SALOME.toProduct())
								.setOrderedQuantity(12)
								.setNegotiatedPriceNet(
										SALOME.getRecommendedPriceNet())
								.build(),
						new OrderItemBuilder()
								.setProduct(JUREK.toProduct())
								.setOrderedQuantity(5)
								.setNegotiatedPriceNet(
										JUREK.getRecommendedPriceNet())
								.build());

		static final List<OrderItem> ORDERITEMS_OF_B13 =
				Arrays.<OrderItem> asList(
						new OrderItemBuilder()
								.setProduct(PAUL.toProduct())
								.setOrderedQuantity(4)
								.setNegotiatedPriceNet(
										PAUL.getRecommendedPriceNet())
								.build(),
						new OrderItemBuilder()
								.setProduct(JUREK.toProduct())
								.setOrderedQuantity(27)
								.setNegotiatedPriceNet(
										JUREK.getRecommendedPriceNet())
								.build(),
						new OrderItemBuilder()
								.setProduct(SALOME.toProduct())
								.setOrderedQuantity(8)
								.setNegotiatedPriceNet(
										SALOME.getRecommendedPriceNet())
								.build(),
						new OrderItemBuilder()
								.setProduct(MILADKA.toProduct())
								.setOrderedQuantity(6)
								.setNegotiatedPriceNet(
										MILADKA.getRecommendedPriceNet())
								.build());
		
		static final List<OrderItem> ORDERITEMS_OF_B15 =
                Arrays.<OrderItem> asList(
                        new OrderItemBuilder()
                                .setProduct(MILADKA.toProduct())
                                .setOrderedQuantity(5)
                                .setNegotiatedPriceNet(
                                        MILADKA.getRecommendedPriceNet())
                                .build(),
                        new OrderItemBuilder()
                                .setProduct(PAUL.toProduct())
                                .setOrderedQuantity(8)
                                .setNegotiatedPriceNet(
                                        PAUL.getRecommendedPriceNet())
                                .build(),
                        new OrderItemBuilder()
                                .setProduct(SALOME.toProduct())
                                .setOrderedQuantity(3)
                                .setNegotiatedPriceNet(
                                        SALOME.getRecommendedPriceNet())
                                .build());

		static final Collection<OrderItem> ORDERITEMS_OF_B21 =
				Arrays.<OrderItem> asList(
						new OrderItemBuilder()
								.setProduct(SALOME.toProduct())
								.setOrderedQuantity(17)
								.setNegotiatedPriceNet(
										SALOME.getRecommendedPriceNet())
								.build(),
						new OrderItemBuilder()
								.setProduct(AMY.toProduct())
								.setOrderedQuantity(3)
								.setNegotiatedPriceNet(
										AMY.getRecommendedPriceNet())
								.build());

		static final Collection<OrderItem> ORDERITEMS_OF_B22 =
				Arrays.<OrderItem> asList(
						new OrderItemBuilder()
								.setProduct(JUREK.toProduct())
								.setOrderedQuantity(13)
								.setNegotiatedPriceNet(
										JUREK.getRecommendedPriceNet())
								.build(),
						new OrderItemBuilder()
								.setProduct(PAUL.toProduct())
								.setOrderedQuantity(6)
								.setNegotiatedPriceNet(
										PAUL.getRecommendedPriceNet())
								.build());
	}

}
