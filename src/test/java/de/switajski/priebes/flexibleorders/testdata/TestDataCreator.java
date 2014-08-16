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

import de.switajski.priebes.flexibleorders.domain.Carrier;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.repository.CarrierRepository;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.service.ItemDtoConverterService;
import de.switajski.priebes.flexibleorders.service.process.DeliveryService;
import de.switajski.priebes.flexibleorders.service.process.InvoicingService;
import de.switajski.priebes.flexibleorders.service.process.OrderService;
import de.switajski.priebes.flexibleorders.testhelper.AbstractTestSpringContextTest;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CarrierBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

/**
 * Creates test data in order to ease GUI Testing.
 * 
 * @author Marek Switajski
 *
 */
public class TestDataCreator extends AbstractTestSpringContextTest {

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
	private CarrierRepository carrierRepo;
	
	// @Ignore("This test is to initialize test data for GUI testing")
	@Test
	public void createTestData() {
		createProducts();
		createCustomers();
		createCarriers();

		createYvonnesOrders();
		createNaidasOrders();
	}

	private void createCarriers() {
		carrierRepo.save(TestData.UPS);
		carrierRepo.save(TestData.DHL);
		carrierRepo.flush();
	}

	private void createNaidasOrders() {
		orderService.order(
				TestData.NAIDA.getCustomerNumber(),
				"B21",
				new Date(),
				converterService.convertOrderItems(
						TestData.ORDERITEMS_OF_B21)
				);
		
		Order b22 = orderService.order(
				TestData.NAIDA.getCustomerNumber(),
				"B22",
				new Date(),
				converterService.convertOrderItems(
						TestData.ORDERITEMS_OF_B22)
				);
		
		OrderConfirmation ab22 = orderService.confirm(
				b22.getOrderNumber(),
				"AB22",
				new Date(),
				TestData.DHL.getCarrierNumber(),
				TestData.YVONNE.getShippingAddress(),
				TestData.YVONNE.getInvoiceAddress(),
				converterService.convert(b22));
		
		orderService.cancelReport(ab22.getDocumentNumber());
		
	}

	private void createYvonnesOrders() {
		Order b11 = orderService.order(
				TestData.YVONNE.getCustomerNumber(),
				"B11",
				new Date(),
				converterService.convertOrderItems(
						TestData.ORDERITEMS_OF_B11)
				);

		Order b12 = orderService.order(
				TestData.YVONNE.getCustomerNumber(),
				"B12",
				new Date(),
				converterService.convertOrderItems(
						TestData.ORDERITEMS_OF_B12)
				);
		
		Order b13 = orderService.order(
				TestData.YVONNE.getCustomerNumber(),
				"B13",
				new Date(),
				converterService.convertOrderItems(
						TestData.ORDERITEMS_OF_B13)
				);

		List<ItemDto> b11Andb12 = new ArrayList<ItemDto>();
		b11Andb12.addAll(converterService.convert(b11));
		b11Andb12.addAll(converterService.convert(b12));

		OrderConfirmation ab11 = orderService.confirm(
				b11.getOrderNumber(),
				"AB11",
				new Date(),
				TestData.DHL.getCarrierNumber(),
				TestData.YVONNE.getShippingAddress(),
				TestData.YVONNE.getInvoiceAddress(),
				b11Andb12);
		
		orderService.confirm(
				b11.getOrderNumber(),
				"AB13",
				new Date(),
				TestData.DHL.getCarrierNumber(),
				TestData.YVONNE.getShippingAddress(),
				TestData.YVONNE.getInvoiceAddress(),
				converterService.convert(b13));

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
						extract(itemDtos, TestData.AMY.getProductNumber(), 2),
						extract(itemDtos, TestData.MILADKA.getProductNumber(), 2)));
		DeliveryNotes l12 = deliveryService.deliver(
				"L12",
				"trackNumber12",
				"packageNumber12",
				new Amount(BigDecimal.ONE),
				new Date(),
				Arrays.asList(
						extract(itemDtos, TestData.AMY.getProductNumber(), 3),
						extract(itemDtos, TestData.MILADKA.getProductNumber(), 3)));
		deliveryService.deliver(
				"L13",
				"trackNumber13",
				"packageNumber13",
				new Amount(BigDecimal.ONE),
				new Date(),
				Arrays.asList(
						extract(itemDtos, TestData.SALOME.getProductNumber(), 1),
						extract(itemDtos, TestData.JUREK.getProductNumber(), 5)));
		deliveryService.deliver(
				"L14",
				"trackNumber14",
				"packageNumber14",
				new Amount(BigDecimal.ZERO),
				new Date(),
				Arrays.asList(
						extract(itemDtos, TestData.PAUL.getProductNumber(), 5)));

		List<ItemDto> l11AndL12 = converterService.convertReport(l11);
		l11AndL12.addAll(converterService.convertReport(l12));

		invoicingService.invoice(
				"R11",
				"5 % Skonto, wenn innerhalb 5 Tagen",
				new Date(),
				Arrays.asList(
						extract(l11AndL12, TestData.AMY.getProductNumber(), 5),
						extract(l11AndL12, TestData.MILADKA.getProductNumber(), 5)),
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
	
	private static class TestData{
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

		static final CatalogProduct AMY = new CatalogProductBuilder().amy().build();
		
		static final Carrier UPS = new CarrierBuilder().ups().build();
		
		static final Carrier DHL = new CarrierBuilder().dhl().build();

		static final List<OrderItem> ORDERITEMS_OF_B11 = Arrays
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

		static final List<OrderItem> ORDERITEMS_OF_B12 =
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
		
		static final List<OrderItem> ORDERITEMS_OF_B13 =
				Arrays.<OrderItem> asList(
						new OrderItemBuilder()
								.setProduct(PAUL.toProduct())
								.setOrderedQuantity(4)
								.setNegotiatedPriceNet(PAUL.getRecommendedPriceNet())
								.build(),
						new OrderItemBuilder()
								.setProduct(JUREK.toProduct())
								.setOrderedQuantity(27)
								.setNegotiatedPriceNet(JUREK.getRecommendedPriceNet())
								.build(),
						new OrderItemBuilder()
								.setProduct(SALOME.toProduct())
								.setOrderedQuantity(8)
								.setNegotiatedPriceNet(SALOME.getRecommendedPriceNet())
								.build(),
						new OrderItemBuilder()
								.setProduct(MILADKA.toProduct())
								.setOrderedQuantity(6)
								.setNegotiatedPriceNet(MILADKA.getRecommendedPriceNet())
								.build());

		static final Collection<OrderItem> ORDERITEMS_OF_B21 =
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

		static final Collection<OrderItem> ORDERITEMS_OF_B22 = 
				Arrays.<OrderItem> asList(
						new OrderItemBuilder()
								.setProduct(JUREK.toProduct())
								.setOrderedQuantity(13)
								.setNegotiatedPriceNet(JUREK.getRecommendedPriceNet())
								.build(),
						new OrderItemBuilder()
								.setProduct(PAUL.toProduct())
								.setOrderedQuantity(6)
								.setNegotiatedPriceNet(PAUL.getRecommendedPriceNet())
								.build());
	}

}
