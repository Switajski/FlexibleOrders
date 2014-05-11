package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
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

public class ExploratorySpecificationTest extends AbstractTestSpringContextTest {

	private static final Address ADDRESS = AddressBuilder
			.buildWithGeneratedAttributes(12);

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

	@Test
	public void SumSpecification_shouldSum() {
		// GIVEN
		ConfirmationReport confirmationReport = givenConfirmationReport();
		givenDeliveryReports(confirmationReport);
		
		// WHEN
		reportItemRepository.findAll(new ConfirmationItemToBeShippedSpec());

	}

	private void givenDeliveryReports(ConfirmationReport confirmationReport) {
		DeliveryNotes l11 = orderService.deliver(
				"L11",
				"trackNumber",
				"packNo",
				ADDRESS,
				null,
				getAmyAndMiladka(confirmationReport, 2));
		DeliveryNotes l12 = orderService.deliver(
				"L12",
				"trackNumber",
				"packNo",
				ADDRESS,
				null,
				getAmyAndMiladka(confirmationReport, 3));
		DeliveryNotes l13 = orderService.deliver(
				"L13",
				"trackNumber",
				"packNo",
				ADDRESS,
				null,
				getAmyAndMiladka(confirmationReport, 2));
	}

	private List<ItemDto> getAmyAndMiladka(
			ConfirmationReport confirmationReport, int qty) {
		List<ItemDto> twoAmyAndMiladka = new ArrayList<ItemDto>();

		ItemDto amy = itemDtoConverterService.convert(
				getFirstItemOf(
						CatalogProductBuilder.buildAmy().toProduct(), 
						confirmationReport));
		amy.setQuantityLeft(qty);
		twoAmyAndMiladka.add(amy);

		ItemDto miladka = itemDtoConverterService.convert(
				getFirstItemOf(CatalogProductBuilder
						.buildMiladka()
						.toProduct(), confirmationReport));
		miladka.setQuantityLeft(qty);
		twoAmyAndMiladka.add(miladka);

		return twoAmyAndMiladka;
	}

	private ReportItem getFirstItemOf(Product product, ConfirmationReport report) {
		ReportItem specificReportItem = null;
		for (ReportItem ri : report.getItems()) {
			if (ri.getOrderItem().getProduct().equals(product)){
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
				b11AndB12);
		return confirmationReport;
	}
}
