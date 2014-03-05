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
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OriginSystem;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.HandlingEventBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.ItemBuilder;

//TODO: split application context to use one part as unit test
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class InvoicePdfFileTest {
	
	private static final String INVOICE_PDF_PATH = "src/test/java/de/switajski/priebes/flexibleorders/report/itextpdf/InvoicePdfFileTest.pdf";

	Invoice invoice;
	
	private static final String I_NR = "13456";
	
	@Before
	public void initData(){
		invoice = new Invoice(I_NR, "paymentCondition", AddressBuilder.buildWithGeneratedAttributes(123));

		OrderItem item1 = new ItemBuilder(
			new FlexibleOrder(
				"email@nowhere.com", 
				OriginSystem.FLEXIBLE_ORDERS, 
				I_NR),
			CatalogProductBuilder.buildWithGeneratedAttributes(98760).toProduct(), 
			0)
		.generateAttributes(12)
		.build();
		
		item1.addHandlingEvent(
			new HandlingEventBuilder(
					HandlingEventType.SHIP, item1, 12)
			.setReport(invoice)
			.build());
		item1.addHandlingEvent(
				new HandlingEventBuilder(
						HandlingEventType.SHIP, item1, 13)
				.setReport(invoice)
				.build()
		);
		
	}
	
	@Ignore
	@Transactional
	@Test
	public void shouldGenerateInvoice() throws Exception{
		
		InvoicePdfFile bpf = new InvoicePdfFile();
		bpf.setFilePathAndName(INVOICE_PDF_PATH);
        
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("Invoice", invoice);

		bpf.render(model, new MockHttpServletRequest(), new MockHttpServletResponse());

	}
}
