package de.switajski.priebes.flexibleorders.itextpdf;

import java.math.BigDecimal;
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

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.reference.OriginSystem;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.InvoiceItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.web.itextpdf.InvoicePdfFile;

//TODO: split application context to use one part as unit test
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class InvoicePdfFileTest {

	private static final String INVOICE_PDF_PATH = "src/test/resources/InvoicePdfFileTest.pdf";

	Invoice invoice;

	private static final String I_NR = "13456";

	@Before
	public void initData() {
		invoice = new Invoice(
				I_NR,
				"paymentCondition",
				AddressBuilder.buildWithGeneratedAttributes(123));
		invoice.setShippingCosts(new Amount(BigDecimal.TEN, Currency.EUR));

		OrderItem item1 = new OrderItemBuilder(
				new Order(
						"email@nowhere.com",
						OriginSystem.FLEXIBLE_ORDERS,
						I_NR),
				CatalogProductBuilder
						.buildWithGeneratedAttributes(98760)
						.toProduct(),
				0)
				.generateAttributes(12)
				.build();

		OrderItem shipping = new OrderItemBuilder(
				new Order(
						"email@nowhere.com",
						OriginSystem.FLEXIBLE_ORDERS,
						I_NR),
				new CatalogProductBuilder("hfhf", 0L, ProductType.PRODUCT)
						.setRecommendedPriceNet(
								new Amount(
										BigDecimal.valueOf(4.5d),
										Currency.EUR))
						.build()
						.toProduct(),
				4)
				.generateAttributes(12)
				.build();
		for (int i = 0; i < 24; i++) {
			item1.addHandlingEvent(
					new InvoiceItemBuilder()
							.setItem(item1)
							.setQuantity(i+1)
							.setReport(invoice)
							.build());
		}
		shipping.addHandlingEvent(
				new InvoiceItemBuilder()
						.setItem(shipping)
						.setQuantity(4)
						.setReport(invoice)
						.build());

	}

	@Transactional(readOnly = true)
	@Test
	public void shouldGenerateInvoice() throws Exception {

		InvoicePdfFile bpf = new InvoicePdfFile();
		bpf.setFilePathAndName(INVOICE_PDF_PATH);
		bpf.setLogoPath("C:/workspaces/gitRepos/FlexibleOrders/src/main/webapp/images/LogoGross.jpg");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("Invoice", invoice);

		bpf.render(
				model,
				new MockHttpServletRequest(),
				new MockHttpServletResponse());

	}
}
