package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
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
import de.switajski.priebes.flexibleorders.repository.ArchiveItemRepository;
import de.switajski.priebes.flexibleorders.repository.InvoiceItemRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;


@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class TransitionServiceTest {

	@Autowired ArchiveItemRepository archiveItemRepository;
	@Autowired CustomerService customerService;
	@Autowired InvoiceItemRepository invoiceItemRepository;
	@Autowired OrderItemService orderItemService;
	@Autowired OrderItemRepository orderItemRepository;
	@Autowired ProductService productService;
	@Autowired ShippingItemRepository shippingItemRepository;
	@Autowired TransitionService transitionService;
	
	private CustomerDataOnDemand cDod;
	private ProductDataOnDemand pDod;
	
	private final static Long PRODUCT_NR = 98134756l;
	private final static int CUSTOMER_NR = 1234;
	private static final int QUANTITY_INITIAL = 4;
	private static final int QUANTITY_CONFIRMED = 2;
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
		
		ShippingItem si = transitionService.confirm(ORDER_NR, 
				productService.findByProductNumber(PRODUCT_NR), 
				QUANTITY_INITIAL, 
				false, ORDER_CONFIRMATION_NR);
		
		assertGivenConfirmedState(oi, si);
	}
	
	@Rollback
	@Test
	public void shouldDeconfirmShippingItem(){
		OrderItem oi = getGivenOrderItem();
		orderItemRepository.saveAndFlush(oi);
		
		ShippingItem si = transitionService.confirm(ORDER_NR, 
				productService.findByProductNumber(PRODUCT_NR), 
				QUANTITY_INITIAL, 
				false, ORDER_CONFIRMATION_NR);
		
		transitionService.deconfirm(ORDER_NR, productService.findByProductNumber(PRODUCT_NR), ORDER_CONFIRMATION_NR);
		
		assertGivenDeconfirmedState(oi, si);
	}

	private void assertGivenDeconfirmedState(OrderItem oi, ShippingItem si) {
		// assert proper order item state
		List<OrderItem> orderItems = orderItemRepository.findByOrderNumberAndProduct(ORDER_NR, oi.getProduct());
		assertFalse("no order item with given orderNumber found!", orderItems.isEmpty());
		OrderItem oiIs = orderItems.get(0);
		assertEquals("Status not set to ORDERED", oiIs.getStatus(), Status.ORDERED);
		assertEquals("OrderNumber of orderItem should be same as given", oi.getOrderNumber(), oiIs.getOrderNumber());
		assertEquals("OrderConfirmation of should be null", null, oi.getOrderConfirmationNumber());
		assertTrue(oiIs.getQuantityLeft() == QUANTITY_INITIAL);

		// assert proper shipping item state
		List<ShippingItem> siItems = shippingItemRepository.findByOrderConfirmationNumber(ORDER_CONFIRMATION_NR);
		assertEquals("should not find shippingItem with given orderConfirmationNumber and productNumber", siItems.isEmpty(), true);

	}

	private void assertGivenConfirmedState(OrderItem oi, ShippingItem si) {
		// assert proper order item state
		List<OrderItem> orderItems = orderItemRepository.findByOrderNumberAndProduct(ORDER_NR, oi.getProduct());
		assertFalse("no order item with given orderNumber found!", orderItems.isEmpty());
		OrderItem equivalentOi = orderItems.get(0);
		assertEquals("Status not set to CONFIRMED", equivalentOi.getStatus(), Status.CONFIRMED);
		assertEquals("OrderNumber of orderItem should be same as given", oi.getOrderNumber(), equivalentOi.getOrderNumber());
		assertEquals("OrderConfirmation of orderItem should be same as given", oi.getOrderConfirmationNumber(), equivalentOi.getOrderConfirmationNumber());
		assertEquals(oi.getQuantity(), QUANTITY_INITIAL);
		assertEquals((oi.getQuantityLeft() == 0), true);
		
		// assert proper shipping item state
		List<ShippingItem> siItems = shippingItemRepository.findByOrderConfirmationNumber(ORDER_CONFIRMATION_NR);
		assertFalse("no shipping item with given orderConfirmationNumber found!", siItems.isEmpty());
		assertEquals("shipping item in repository should be the same as the one returned in OrderItem.confirm()", si, siItems.get(0));
	}
	
	private void assertGivenPartialConfirmedState(OrderItem oi, ShippingItem si){
		// assert proper order item state
		List<OrderItem> orderItems = orderItemRepository.findByOrderNumberAndProduct(ORDER_NR, oi.getProduct());
		assertFalse("no order item with given orderNumber found!", orderItems.isEmpty());
		OrderItem equivalentOi = orderItems.get(0);
				
		Status shouldBe;
		if (QUANTITY_CONFIRMED==QUANTITY_INITIAL) shouldBe = Status.CONFIRMED;
		else shouldBe = Status.ORDERED;
		assertEquals("Status should be only confirmed when quantity_left equals quantity", 
				equivalentOi.getStatus(), shouldBe);
		assertEquals((oi.getQuantityLeft() == (QUANTITY_INITIAL - QUANTITY_CONFIRMED)), true);

	}

	private OrderItem getGivenOrderItem() {
		Product product = pDod.getSpecificProduct(Integer.valueOf(PRODUCT_NR.toString()));
		product.setPriceNet(BigDecimal.TEN);
		product.setProductNumber(PRODUCT_NR);
		productService.saveProduct(product);
		
		Customer customer = cDod.getSpecificCustomer(CUSTOMER_NR);
		
		OrderItem oi = new OrderItem(); 
		oi.setInitialState(product, customer, QUANTITY_INITIAL, ORDER_NR);
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
