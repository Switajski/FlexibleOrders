package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.specification.OpenShippingItemSpec;
import de.switajski.priebes.flexibleorders.service.process.DeliveryService;
import de.switajski.priebes.flexibleorders.service.process.OrderService;
import de.switajski.priebes.flexibleorders.testhelper.AbstractTestSpringContextTest;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class SpecificationIntegrationTest extends AbstractTestSpringContextTest {

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

	private static final int JUREK_QTY = 5; // All Jureks from B12

	private static final Product AMY = CatalogProductBuilder
			.buildAmy()
			.toProduct();

	private static final Product PAUL = CatalogProductBuilder
			.buildPaul()
			.toProduct();

	private static final Product MILADKA = CatalogProductBuilder
			.buildMiladka()
			.toProduct();

	private static final Product SALOME = CatalogProductBuilder
			.buildSalome()
			.toProduct();

	private static final Product JUREK = CatalogProductBuilder
			.buildJurek()
			.toProduct();

	@Transactional
	@Test
	public void findAll_OpenShippingItemSpecShouldRetrieveSpecifiedReportItems() {
		// GIVEN test data from Open Office calculation sheet "Test Data.ods"
		OrderAgreement agreement = givenOrderAgreement();
		givenDeliveryReports(agreement);

		// WHEN
		List<ReportItem> retrievedRis = reportItemRepository
				.findAll(new OpenShippingItemSpec());

		// THEN
		assertThat(retrievedRis.isEmpty(), is(false));
		assertAllItemsAreConfirmationItems(retrievedRis);
		assertConfirmationItemHaveNoJurek(retrievedRis);
	}

	private void assertAllItemsAreConfirmationItems(
			List<ReportItem> retrievedRis) {
		for (ReportItem ri : retrievedRis) {
			assertThat(ri instanceof ConfirmationItem, is(true));
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
	 * @param orderAgreement
	 */
	private void givenDeliveryReports(OrderAgreement orderAgreement) {
		deliveryService.deliver(
				"L11",
				"trackNumber",
				"packNo",
				null,
				new Date(),
				Arrays.asList(
						createItemDto(2, AMY, orderAgreement),
						createItemDto(2, MILADKA, orderAgreement)));

		deliveryService.deliver(
				"L12",
				"trackNumber",
				"packNo",
				null,
				new Date(),
				Arrays.asList(
						createItemDto(3, AMY, orderAgreement),
						createItemDto(3, MILADKA, orderAgreement)));

		deliveryService.deliver(
				"L13",
				"trackNumber",
				"packNo",
				null,
				new Date(),
				Arrays.asList(
						createItemDto(1, SALOME, orderAgreement),
						createItemDto(JUREK_QTY, JUREK, orderAgreement)));

		deliveryService.deliver(
				"L14",
				"trackNumber",
				"packNo",
				null,
				new Date(),
				Arrays.asList(
						createItemDto(5, PAUL, orderAgreement)));
	}

	private ItemDto createItemDto(int qty, Product product,
			OrderAgreement agreement) {
		ItemDto item = itemDtoConverterService.convert(
				getFirstItemOf(
						product,
						agreement));
		item.setQuantityLeft(qty);
		return item;
	}

	private ReportItem getFirstItemOf(Product product, OrderAgreement report) {
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
	 * AU11: </br>
	 * same as AB11
	 * 
	 * @return
	 */
	private OrderAgreement givenOrderAgreement() {
		Order b11 = OrderBuilder.B11();
		Customer yvonne = customerService.create(b11.getCustomer());
		orderRepo.save(b11);

		Order b12 = OrderBuilder.B12();
		b12.setCustomer(yvonne);
		orderRepo.save(b12);

		List<ItemDto> b11AndB12 = itemDtoConverterService.convert(b11);
		b11AndB12.addAll(itemDtoConverterService.convert(b12));

		OrderConfirmation confirmationReport = orderService.confirm(
				b11.getOrderNumber(),
				"AB11",
				new Date(),
				AddressBuilder.createDefault(),
				AddressBuilder.createDefault(),
				b11AndB12);
		
		OrderAgreement orderAgreement = orderService.agree(confirmationReport.getDocumentNumber(), "AU11");
		return orderAgreement;
	}
}
