package de.switajski.priebes.flexibleorders.report.itextpdf;

import static org.junit.Assert.*;

import java.util.ArrayList;
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

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OrderItemDataOnDemand;
import de.switajski.priebes.flexibleorders.report.Order;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.OrderService;
import de.switajski.priebes.flexibleorders.service.ShippingItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class OrderPdfFileTest {
	
	@Autowired OrderService orderService;
	@Autowired OrderItemService orderItemService;
	@Autowired ShippingItemService shippingItemService;
	
	public static final String RESULT
    = "src/test/java/de/switajski/priebes/flexibleorders/report/itextpdf/Order12378.pdf";
	
	Order order;
	
	@Before
	public void initData(){
		OrderItemDataOnDemand dod = new OrderItemDataOnDemand();
		OrderItem oi1 = dod.getRandomOrderItem();
		OrderItem oi2 = dod.getRandomOrderItem();
		
		oi2.setOrderNumber(oi1.getOrderNumber());
		OrderItem merged = (OrderItem) orderItemService.updateOrderItem(oi2);
		
		ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
		orderItems.add(oi1);
		orderItems.add(merged);
		
		order = new Order(orderItems);
		
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
