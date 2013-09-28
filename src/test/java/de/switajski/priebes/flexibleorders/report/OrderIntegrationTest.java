package de.switajski.priebes.flexibleorders.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OrderItemDataOnDemand;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.OrderService;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class OrderIntegrationTest {

	@Autowired OrderItemService orderItemService;
	@Autowired CustomerService customerService;
	@Autowired OrderService orderService;
	
	@Test
	public void shouldGetPageableOrders(){
		Page<OrderItem> pages = orderItemService.findAll(new PageRequest(0,20));
		assertEquals(pages.getSize(), 20);
	}
	
	@Test
	public void shouldCountOrders(){
		Long ordersSize = orderService.countAll();
		assertTrue(ordersSize != 0);
	}
	
	@Test
	public void shouldGetOrdernumbersByCustomer(){
		Customer customer = customerService.findCustomer(1l);
		Page<Long> orderNumbers = orderService.getOrderNumbersByCustomer(customer, new PageRequest(0,5));
		assertTrue(orderNumbers.getSize() != 0);
	}
	
	@Test
	public void shouldFindAll(){
		Page<Order> orders = orderService.findAll(new PageRequest(0,5));
		assertFalse(orders.getSize()==0);
	}
	
	@Test
	public void shouldCreateOrders(){
		OrderItemDataOnDemand dod = new OrderItemDataOnDemand();
		OrderItem oi1 = dod.getRandomOrderItem();
		OrderItem oi2 = dod.getRandomOrderItem();
		
		oi2.setOrderNumber(oi1.getOrderNumber());
		OrderItem merged = (OrderItem)orderItemService.updateOrderItem(oi2);
		
		ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
		orderItems.add(oi1);
		orderItems.add(merged);
		
		Order order = new Order(orderItems);
		assertFalse(order.getNetAmount().equals(BigDecimal.ZERO));
		assertEquals(order.getItems().size(), 2);
		
	}
	
}
