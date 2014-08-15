package de.switajski.priebes.flexibleorders.testdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.service.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.DeliveryService;
import de.switajski.priebes.flexibleorders.service.process.InvoicingService;
import de.switajski.priebes.flexibleorders.service.process.OrderService;
import de.switajski.priebes.flexibleorders.testhelper.AbstractTestSpringContextTest;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class TestDataCreator extends AbstractTestSpringContextTest {

	private static final Customer YVONNE = CustomerBuilder.buildYvonne();

	private static final Customer WYOMING = CustomerBuilder.buildWyoming();

	private static final Customer NAIDA = CustomerBuilder.buildNaida();

	private static final Customer JEROME = CustomerBuilder.buildJerome();

	private static final Customer EDWARD = CustomerBuilder.buildEdward();

	private static final CatalogProduct SALOME = CatalogProductBuilder
			.buildSalome();

	private static final CatalogProduct PAUL = CatalogProductBuilder
			.buildPaul();

	private static final CatalogProduct MILADKA = CatalogProductBuilder
			.buildMiladka();

	private static final CatalogProduct JUREK = CatalogProductBuilder
			.buildJurek();

	private static final CatalogProduct AMY = CatalogProductBuilder.buildAmy();

	private static final List<OrderItem> ORDERITEMS_OF_B11 = Arrays
			.<OrderItem> asList(
					new OrderItemBuilder()
							.setProduct(AMY.toProduct())
							.setOrderedQuantity(10)
							.setNegotiatedPriceNet(AMY.getRecommendedPriceNet())
							.build(),
					new OrderItemBuilder()
							.setProduct(MILADKA.toProduct())
							.setOrderedQuantity(15)
							.setNegotiatedPriceNet(MILADKA.getRecommendedPriceNet())
							.build(),
					new OrderItemBuilder()
							.setProduct(PAUL.toProduct())
							.setOrderedQuantity(30)
							.setNegotiatedPriceNet(PAUL.getRecommendedPriceNet())
							.build());

	private static final List<OrderItem> ORDERITEMS_OF_B12 =
			Arrays.<OrderItem> asList(
					new OrderItemBuilder()
							.setProduct(SALOME.toProduct())
							.setOrderedQuantity(12)
							.setNegotiatedPriceNet(SALOME.getRecommendedPriceNet())
							.build(),
					new OrderItemBuilder()
							.setProduct(JUREK.toProduct())
							.setOrderedQuantity(5)
							.setNegotiatedPriceNet(JUREK.getRecommendedPriceNet())
							.build());

	private static final Collection<OrderItem> ORDERITEMS_OF_B21 =
			Arrays.<OrderItem> asList(
					new OrderItemBuilder()
							.setProduct(SALOME.toProduct())
							.setOrderedQuantity(17)
							.setNegotiatedPriceNet(SALOME.getRecommendedPriceNet())
							.build(),
					new OrderItemBuilder()
							.setProduct(AMY.toProduct())
							.setOrderedQuantity(3)
							.setNegotiatedPriceNet(AMY.getRecommendedPriceNet())
							.build());

	private static final Collection<OrderItem> ORDERITEMS_OF_B22 = 
			Arrays.<OrderItem> asList(
					new OrderItemBuilder()
							.setProduct(JUREK.toProduct())
							.setOrderedQuantity(17)
							.setNegotiatedPriceNet(JUREK.getRecommendedPriceNet())
							.build(),
					new OrderItemBuilder()
							.setProduct(PAUL.toProduct())
							.setOrderedQuantity(6)
							.setNegotiatedPriceNet(PAUL.getRecommendedPriceNet())
							.build());

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

	// @Ignore("This test is to initialize test data for GUI testing")
	@Test
	public void createTestData() {
		createProducts();
		createCustomers();

		createYvonnesOrders();
		createNaidasOrders();
	}

	private void createNaidasOrders() {
		orderService.order(
				NAIDA.getCustomerNumber(),
				"B21",
				new Date(),
				converterService.convertOrderItems(
						ORDERITEMS_OF_B21)
				);
		
		Order b22 = orderService.order(
				NAIDA.getCustomerNumber(),
				"B22",
				new Date(),
				converterService.convertOrderItems(
						ORDERITEMS_OF_B22)
				);
		
		OrderConfirmation ab22 = orderService.confirm(
				b22.getOrderNumber(),
				"AB22",
				new Date(),
				YVONNE.getShippingAddress(),
				YVONNE.getInvoiceAddress(),
				converterService.convert(b22));
		
		orderService.cancelReport(ab22.getDocumentNumber());
		
	}

	private void createYvonnesOrders() {
		Order b11 = orderService.order(
				YVONNE.getCustomerNumber(),
				"B11",
				new Date(),
				converterService.convertOrderItems(
						ORDERITEMS_OF_B11)
				);

		Order b12 = orderService.order(
				YVONNE.getCustomerNumber(),
				"B12",
				new Date(),
				converterService.convertOrderItems(
						ORDERITEMS_OF_B12)
				);

		List<ItemDto> b11Andb12 = new ArrayList<ItemDto>();
		b11Andb12.addAll(converterService.convert(b11));
		b11Andb12.addAll(converterService.convert(b12));

		OrderConfirmation ab11 = orderService.confirm(
				b11.getOrderNumber(),
				"AB11",
				new Date(),
				YVONNE.getShippingAddress(),
				YVONNE.getInvoiceAddress(),
				b11Andb12);

		OrderAgreement au11 = orderService.agree(
				ab11.getDocumentNumber(),
				"AU11");

		List<ItemDto> itemDtos = converterService.convertReport(au11);

		DeliveryNotes l11 = deliveryService.deliver(
				"L11",
				"trackNumber",
				"packageNumber",
				new Amount(BigDecimal.TEN),
				new Date(),
				Arrays.asList(
						extract(itemDtos, AMY.getProductNumber(), 2),
						extract(itemDtos, MILADKA.getProductNumber(), 2)));
		DeliveryNotes l12 = deliveryService.deliver(
				"L12",
				"trackNumber12",
				"packageNumber12",
				new Amount(BigDecimal.ONE),
				new Date(),
				Arrays.asList(
						extract(itemDtos, AMY.getProductNumber(), 3),
						extract(itemDtos, MILADKA.getProductNumber(), 3)));
		deliveryService.deliver(
				"L13",
				"trackNumber13",
				"packageNumber13",
				new Amount(BigDecimal.ONE),
				new Date(),
				Arrays.asList(
						extract(itemDtos, SALOME.getProductNumber(), 1),
						extract(itemDtos, JUREK.getProductNumber(), 5)));
		deliveryService.deliver(
				"L14",
				"trackNumber14",
				"packageNumber14",
				new Amount(BigDecimal.ZERO),
				new Date(),
				Arrays.asList(
						extract(itemDtos, PAUL.getProductNumber(), 5)));

		List<ItemDto> l11AndL12 = converterService.convertReport(l11);
		l11AndL12.addAll(converterService.convertReport(l12));

		invoicingService.invoice(
				"R11",
				"5 % Skonto, wenn innerhalb 5 Tagen",
				new Date(),
				Arrays.asList(
						extract(l11AndL12, AMY.getProductNumber(), 5),
						extract(l11AndL12, MILADKA.getProductNumber(), 5)),
				"billing");
	}

	private ItemDto extract(List<ItemDto> itemDtos, Long productNumber, int i) {
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
