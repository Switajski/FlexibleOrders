package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OriginSystem;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.ItemBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class OrderPdfFileTest {

	private final static String O_PDF_FILE = "src/test/java/de/switajski/priebes/flexibleorders/report/itextpdf/OrderPdfFileTest.pdf";
	
	private FlexibleOrder order;
	
	@Before
	public void initData(){
		order = new FlexibleOrder(CustomerBuilder.buildWithGeneratedAttributes(123), 
				OriginSystem.FLEXIBLE_ORDERS, "123561");
		
		OrderItem item1 = new ItemBuilder(
				order,
				CatalogProductBuilder.buildWithGeneratedAttributes(98760).toProduct(), 
				5)
			.generateAttributes(15)
			.build();
		
		OrderItem item2 = new ItemBuilder(
				order,
				CatalogProductBuilder.buildWithGeneratedAttributes(98760).toProduct(), 
				12)
			.generateAttributes(12)
			.build();
		
		order.addOrderItem(item1);
		order.addOrderItem(item2);
	}
	
	@Ignore
	@Transactional
	@Test
	public void shouldGenerateOrder() throws Exception{
		OrderPdfFile bpf = new OrderPdfFile();
		bpf.setFileNameAndPath(O_PDF_FILE);

		Map<String,Object> model = new HashMap<String,Object>();
		model.put("Order", order);

		bpf.render(model, new MockHttpServletRequest(), new MockHttpServletResponse());

	}
}
