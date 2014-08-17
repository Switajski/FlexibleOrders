package de.switajski.priebes.flexibleorders.itextpdf;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.reference.OriginSystem;
import de.switajski.priebes.flexibleorders.testhelper.AbstractSpringContextTest;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ConfirmationItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.ContactInformationBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerDetailsBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.web.itextpdf.OrderConfirmationPdfFile;

public class OrderConfirmationPdfFileTest extends AbstractSpringContextTest{

	OrderConfirmation orderConfirmation;

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

		orderConfirmation = new OrderConfirmation(
				OC_NR,
				AddressBuilder.buildWithGeneratedAttributes(123),
				AddressBuilder.buildWithGeneratedAttributes(6623));
		orderConfirmation.setCustomerDetails(createCustomerDetails());
		orderConfirmation.getAgreementDetails().setExpectedDelivery(new Date());

		for (int i = 0; i < 28; i++) {
			item1.addReportItem(
					new ConfirmationItemBuilder()
							.setQuantity(i)
							.setItem(item1)
							.setReport(orderConfirmation)
							.build());
		}

	}

	private CustomerDetails createCustomerDetails() {
		CustomerDetails cd = new CustomerDetailsBuilder()
		.setPaymentConditions("So schnell wie möglich, ohne Prozente sonst Inkasso Moskau")
		.setVatIdNo("ATU-No.111234515")
		.setVendorNumber("PRIEBES-1")
		.setSaleRepresentative("Herr Vertreter1")
		.setContactInformation(new ContactInformationBuilder()
			.setContact1("Ihr Ansprechpartner: Hr. Priebe")
			.setContact2("Mobil: 0175 / 124312541")
			.setContact3("Fax: 0175 / 12431241")
			.setContact4("Email: info@priebe.eu")
			.build())
		.setMark("Filiale in LB")
		.build();
		return cd;
	}

	@Transactional
	@Test
	public void shouldGenerateOrder() throws Exception {

		OrderConfirmationPdfFile bpf = new OrderConfirmationPdfFile();
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
