package de.switajski.priebes.flexibleorders.itextpdf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.testhelper.AbstractSpringContextTest;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.OrderPdfFile;

public class OrderPdfFileTest {

	private final static String O_PDF_FILE = "src/test/resources/OrderPdfFileTest.pdf";

	private Order order;

	private ReportDto addOrderItems(ReportDto dto) {
		dto.orderItems = new HashSet<OrderItem>(Arrays.asList(
		new OrderItemBuilder(
				order,
				CatalogProductBuilder
						.buildWithGeneratedAttributes(98760)
						.toProduct(),
				5)
				.generateAttributes(15)
				.build(), 
		new OrderItemBuilder(
				order,
				CatalogProductBuilder
						.buildWithGeneratedAttributes(98760)
						.toProduct(),
				12)
				.generateAttributes(12)
				.build()));
		return dto;
	}

	@Transactional
	@Test
	public void shouldGenerateOrder() throws Exception {
		OrderPdfFile bpf = new OrderPdfFile();
		bpf.setFileNameAndPath(O_PDF_FILE);
		bpf.setLogoPath("C:/workspaces/gitRepos/FlexibleOrders/src/main/webapp/images/LogoGross.jpg");

		Map<String, Object> model = new HashMap<String, Object>();
		ReportDto reportDto = addOrderItems(ReportDtoTestFixture.givenReportDto());
        model.put(reportDto.getClass().getSimpleName(), reportDto);

		bpf.render(
				model,
				new MockHttpServletRequest(),
				new MockHttpServletResponse());

	}
}
