package de.switajski.priebes.flexibleorders.itextpdf;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.reference.OriginSystem;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.web.itextpdf.ConfirmationReportPdfFile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class ConfirmationReportPdfFileTest {

	ConfirmationReport orderConfirmation;

	private static final String OC_NR = "98732645";
	private static final String O_NR = "3465897";

	private static final String OC_PDF_FILE = "src/test/resources/ConfirmationReportPdfFileTest.pdf";

	@Before
	public void initData() {
		OrderItem item1 = new OrderItemBuilder(
				new Order(
						"email@nowhere.com",
						OriginSystem.FLEXIBLE_ORDERS,
						O_NR),
				CatalogProductBuilder
						.buildWithGeneratedAttributes(98760)
						.toProduct(),
				0)
				.generateAttributes(12)
				.build();

		orderConfirmation = new ConfirmationReport(
				OC_NR,
				AddressBuilder.buildWithGeneratedAttributes(123),
				AddressBuilder.buildWithGeneratedAttributes(123));

		for (int i = 0; i < 28; i++) {
			item1.addHandlingEvent(
					new ConfirmationItemBuilder()
							.setQuantity(i)
							.setItem(item1)
							.setReport(orderConfirmation)
							.build());
		}

	}

	@Transactional
	@Test
	public void shouldGenerateOrder() throws Exception {

		ConfirmationReportPdfFile bpf = new ConfirmationReportPdfFile();
		bpf.setFilePathAndName(OC_PDF_FILE);
		bpf
				.setLogoPath("C:/workspaces/gitRepos/FlexibleOrders/src/main/webapp/images/LogoGross.jpg");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("ConfirmationReport", orderConfirmation);

		bpf.render(
				model,
				new MockHttpServletRequest(),
				new MockHttpServletResponse());

	}

}
