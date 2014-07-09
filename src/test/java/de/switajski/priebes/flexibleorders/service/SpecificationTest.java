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

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.specification.ConfirmationItemToBeShippedSpec;
import de.switajski.priebes.flexibleorders.testhelper.AbstractTestSpringContextTest;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class SpecificationTest extends AbstractTestSpringContextTest {

	@Autowired
	private CustomerServiceImpl customerService;

	@Autowired
	private CatalogProductServiceImpl productService;

	@Autowired
	private OrderServiceImpl orderService;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private ItemDtoConverterService itemDtoConverterService;

	@Autowired
	private ReportItemRepository reportItemRepository;
	
	private static final int JUREK_QTY = 5; //All Jureks from B12

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

	private static final Address ADDRESS = AddressBuilder
			.buildWithGeneratedAttributes(12);
	
	@Transactional
	@Test
	public void SumSpecification_shouldSum() {
		// GIVEN
		ConfirmationReport confirmationReport = givenConfirmationReport();
		givenDeliveryReports(confirmationReport);

		// WHEN
		List<ReportItem> retrievedRis = reportItemRepository
				.findAll(new ConfirmationItemToBeShippedSpec());

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

	private void givenDeliveryReports(ConfirmationReport confirmationReport) {
		orderService.deliver("L11", "trackNumber", "packNo", null, new Date(), 
				Arrays.asList(
						createItemDto(2, AMY, confirmationReport),
						createItemDto(2, MILADKA, confirmationReport)));
		
		orderService.deliver("L12", "trackNumber", "packNo", null, new Date(),
				Arrays.asList(
						createItemDto(3, AMY, confirmationReport),
						createItemDto(3, MILADKA, confirmationReport)));
		
		orderService.deliver("L13", "trackNumber", "packNo", null, new Date(),
				Arrays.asList(
						createItemDto(1, SALOME, confirmationReport),
						createItemDto(JUREK_QTY, JUREK, confirmationReport)));
		
		orderService.deliver("L14", "trackNumber", "packNo", null, new Date(),
				Arrays.asList(
						createItemDto(5, PAUL, confirmationReport)));
	}

	private ItemDto createItemDto(int qty, Product product,
			ConfirmationReport confirmationReport) {
		ItemDto item = itemDtoConverterService.convert(
				getFirstItemOf(
						product,
						confirmationReport));
		item.setQuantityLeft(qty);
		return item;
	}

	private ReportItem getFirstItemOf(Product product, ConfirmationReport report) {
		ReportItem specificReportItem = null;
		for (ReportItem ri : report.getItems()) {
			if (ri.getOrderItem().getProduct().equals(product)) {
				specificReportItem = ri;
				break;
			}
		}
		return specificReportItem;
	}

	private ConfirmationReport givenConfirmationReport() {
		Order b11 = OrderBuilder.B11();
		Customer yvonne = customerService.create(b11.getCustomer());
		orderRepo.save(b11);

		Order b12 = OrderBuilder.B12();
		b12.setCustomer(yvonne);
		orderRepo.save(b12);

		List<ItemDto> b11AndB12 = itemDtoConverterService.convert(b11);
		b11AndB12.addAll(itemDtoConverterService.convert(b12));

		ConfirmationReport confirmationReport = orderService.confirm(
				b11.getOrderNumber(),
				"AB11",
				new Date(),
				null, null, b11AndB12);
		return confirmationReport;
	}
}
