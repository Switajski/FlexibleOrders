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
import de.switajski.priebes.flexibleorders.domain.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class OrderConfirmationPdfFileTest {
		
	OrderConfirmation orderConfirmation;
	
	private static final long OC_NR = 98732645l;
	private static final Long O_NR = 3465897l;
	
	@Before
	public void initData(){
		EntityBuilder eb = new EntityBuilder();
		ShippingItem i1 = eb.getShippingItem(15.59, O_NR, OC_NR);
		ShippingItem i2 = eb.getShippingItem(25.99, O_NR, OC_NR);
				
		ArrayList<ShippingItem> items = new ArrayList<ShippingItem>();
		items.add(i1);
		items.add(i2);
		
		orderConfirmation = new OrderConfirmation(items);
	}
	
	@Transactional
	@Test
	public void shouldGenerateOrder(){

		OrderConfirmationPdfFile bpf = new OrderConfirmationPdfFile();
        
		try {
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("OrderConfirmation", orderConfirmation);
			
			bpf.render(model, new MockHttpServletRequest(), new MockHttpServletResponse());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}
	
	
}
