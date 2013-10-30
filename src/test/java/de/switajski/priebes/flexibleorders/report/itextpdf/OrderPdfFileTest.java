package de.switajski.priebes.flexibleorders.report.itextpdf;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.report.Order;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.ShippingItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class OrderPdfFileTest {
	
	@Autowired OrderItemService orderItemService;
	@Autowired ShippingItemService shippingItemService;
	
	Order order;
	
	@Before
	public void initData(){
		//TODO: create a mock
//		OrderItem oi1 = OrderItemTestFixture.createRandom();
//		OrderItem oi2 = OrderItemTestFixture.createRandom();
//		
//		oi2.setOrderNumber(oi1.getOrderNumber());
//		OrderItem merged = (OrderItem) orderItemService.updateOrderItem(oi2);
//		
//		ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
//		orderItems.add(oi1);
//		orderItems.add(merged);
//		
//		order = new Order(orderItems);
	}
	
	@Transactional
	@Test
	public void shouldGenerateOrder(){

		
		OrderPdfFile bpf = new OrderPdfFile();
        
		try {
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("Order", order);
			
			bpf.render(model, new MockHttpServletRequest(), new MockHttpServletResponse());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}
}
