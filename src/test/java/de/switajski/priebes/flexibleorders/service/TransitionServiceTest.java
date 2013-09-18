package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.CustomerDataOnDemand;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OrderItemDataOnDemand;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ProductDataOnDemand;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.report.Order;
import de.switajski.priebes.flexibleorders.repository.ArchiveItemRepository;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;


@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class TransitionServiceTest {

	@Autowired OrderItemRepository orderItemRepository;
	@Autowired ShippingItemRepository shippingItemRepository;
	@Autowired InvoiceItemRepository invoiceItemRepository;
	@Autowired CustomerService customerService;
	@Autowired ArchiveItemRepository archiveItemRepository;
	@Autowired OrderItemService orderItemService;
	
	private CustomerDataOnDemand cDod;
	private ProductDataOnDemand pDod;
	
	private final static int PRODUCT_NR = 98134756;
	private final static int CUSTOMER_NR = 1234;
	private static final int QUANTITY = 4;
	private static final Long ORDER_NR = 1976578l;
	private static final long ORDER_CONFIRMATION_NR = 98756345l;
	
	
	@Before
	public void initDataOnDemand(){
		pDod = new ProductDataOnDemand();
		cDod = new CustomerDataOnDemand();
	}
	
	@Rollback
	@Test
	public void shouldConfirmOrderItem(){
		OrderItem oi = getGivenOrderItem();
		orderItemRepository.saveAndFlush(oi);
		
		ShippingItem si = oi.confirm(false, QUANTITY, ORDER_CONFIRMATION_NR);
		shippingItemRepository.saveAndFlush(si);
		
		assertGivenConfirmedState(oi, si);
	}
	
	@Rollback
	@Test
	public void shouldDeconfirmShippingItem(){
		OrderItem oi = getGivenOrderItem();
		orderItemRepository.saveAndFlush(oi);
		
		ShippingItem si = oi.confirm(false, QUANTITY, ORDER_CONFIRMATION_NR);
		shippingItemRepository.saveAndFlush(si);
		
		si.deconfirm(oi);
	}

	private void assertGivenConfirmedState(OrderItem oi, ShippingItem si) {
		List<OrderItem> orderItems = orderItemRepository.findByOrderNumber(ORDER_NR);
		assertFalse("no order item with given orderNumber found!", orderItems.isEmpty());
		OrderItem equivalentOi = orderItems.get(0);
		assertEquals("Status not set to CONFIRMED", equivalentOi.getStatus(), Status.CONFIRMED);
		assertEquals("OrderNumber of orderItem is wrong", oi.getOrderNumber(), equivalentOi.getOrderNumber());
		assertEquals("OrderConfirmation of orderItem is wrong", oi.getOrderConfirmationNumber(), equivalentOi.getOrderConfirmationNumber());
		
		List<ShippingItem> siItems = shippingItemRepository.findByOrderConfirmationNumber(ORDER_CONFIRMATION_NR);
		assertFalse("no shipping item with given orderConfirmationNumber found!", siItems.isEmpty());
		assertEquals("shipping item in repository is not the same as the one returned in OrderItem.confirm()", si, siItems.get(0));
	}

	private OrderItem getGivenOrderItem() {
		Product product = pDod.getSpecificProduct(PRODUCT_NR);
		product.setPriceNet(BigDecimal.TEN);
		
		Customer customer = cDod.getSpecificCustomer(CUSTOMER_NR);
		
		OrderItem oi = new OrderItem(); 
		oi.setInitialState(product, customer, QUANTITY, ORDER_NR);
		return oi;
	}
	
	@Rollback
	@Test
	public void shouldDeconfirmOrderItem(){
		
	}
	
	@Transactional
	@Test
	public void shouldConfirm(){
		boolean toSupplier = false;
		long orderConfirmationNumber = 1345l;
		
		OrderItemDataOnDemand oiDod = new OrderItemDataOnDemand();
		
		OrderItem orderItem = oiDod.getSpecificOrderItem(12);
		ShippingItem shippingItem = orderItem.confirm(toSupplier, 12, orderConfirmationNumber);
		shippingItemRepository.saveAndFlush(shippingItem);
		orderItemRepository.saveAndFlush(orderItem);
//		pji.importCustomers();
				
	}
	
}
