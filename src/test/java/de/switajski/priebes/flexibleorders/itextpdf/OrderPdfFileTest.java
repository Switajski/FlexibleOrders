package de.switajski.priebes.flexibleorders.itextpdf;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.reference.OriginSystem;
import de.switajski.priebes.flexibleorders.testhelper.AbstractSpringContextTest;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.web.itextpdf.OrderPdfFile;

public class OrderPdfFileTest extends AbstractSpringContextTest{

	private final static String O_PDF_FILE = "src/test/resources/OrderPdfFileTest.pdf";

	private Order order;

	@Before
	public void initData() {
		order = new Order(CustomerBuilder.buildWithGeneratedAttributes(123),
				OriginSystem.FLEXIBLE_ORDERS, "123561");

		OrderItem item1 = new OrderItemBuilder(
				order,
				CatalogProductBuilder
						.buildWithGeneratedAttributes(98760)
						.toProduct(),
				5)
				.generateAttributes(15)
				.build();

		OrderItem item2 = new OrderItemBuilder(
				order,
				CatalogProductBuilder
						.buildWithGeneratedAttributes(98760)
						.toProduct(),
				12)
				.generateAttributes(12)
				.build();

		order.addOrderItem(item1);
		order.addOrderItem(item2);
	}

	@Transactional
	@Test
	public void shouldGenerateOrder() throws Exception {
		OrderPdfFile bpf = new OrderPdfFile();
		bpf.setFileNameAndPath(O_PDF_FILE);
		bpf.setLogoPath("C:/workspaces/gitRepos/FlexibleOrders/src/main/webapp/images/LogoGross.jpg");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put(order.getClass().getSimpleName(), order);

		bpf.render(
				model,
				new MockHttpServletRequest(),
				new MockHttpServletResponse());

	}
}
