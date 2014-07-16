package de.switajski.priebes.flexibleorders.itextpdf;

import java.util.Date;
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
import de.switajski.priebes.flexibleorders.domain.CustomerDetails;
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
				AddressBuilder.buildWithGeneratedAttributes(6623));
		orderConfirmation.setCustomerDetails(createCustomerDetails());
		orderConfirmation.setExpectedDelivery(new Date());

		for (int i = 0; i < 28; i++) {
			item1.addHandlingEvent(
					new ConfirmationItemBuilder()
							.setQuantity(i)
							.setItem(item1)
							.setReport(orderConfirmation)
							.build());
		}

	}

	private CustomerDetails createCustomerDetails() {
		CustomerDetails cd = new CustomerDetails();
		cd.setPaymentConditions("So schnell wie möglich, ohne Prozente sonst Inkasso Moskau");
		cd.setVatIdNo("ATU-No.111234515");
		cd.setVendorNumber("PRIEBES-1");
		cd.setSaleRepresentative("Herr Vertreter1");
		cd.setContact1("Ihr Ansprechpartner: Hr. Priebe");
		cd.setContact2("mobil: 0175 / 1243");
		cd.setContact3("email: info@priebe.eu");
		cd.setMark("Filiale in LB");
		return cd;
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
