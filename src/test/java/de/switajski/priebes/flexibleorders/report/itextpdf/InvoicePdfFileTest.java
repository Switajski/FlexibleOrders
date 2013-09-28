package de.switajski.priebes.flexibleorders.report.itextpdf;

import static org.junit.Assert.fail;

import java.util.ArrayList;
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

import de.switajski.priebes.flexibleorders.domain.EntityBuilder;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.report.Invoice;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class InvoicePdfFileTest {
	
	Invoice invoice;
	
	private static final long OC_NR = 98732645l;
	private static final Long O_NR = 3465897l;
	private static final Long I_NR = 13456l;
	
	@Before
	public void initData(){
		EntityBuilder eb = new EntityBuilder();
		InvoiceItem i1 = eb.getInvoiceItem(15.59, O_NR, OC_NR, I_NR);
		InvoiceItem i2 = eb.getInvoiceItem(25.59, O_NR, OC_NR, I_NR);
		
		ArrayList<InvoiceItem> invoiceItems = new ArrayList<InvoiceItem>();
		invoiceItems.add(i1);
		invoiceItems.add(i2);
		
		invoice = new Invoice(invoiceItems);
	}
	
	@Transactional
	@Test
	public void shouldGenerateInvoice(){
		
		InvoicePdfFile bpf = new InvoicePdfFile();
        
		try {
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("Invoice", invoice);
			
			bpf.render(model, new MockHttpServletRequest(), new MockHttpServletResponse());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}
}
