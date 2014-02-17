package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Currency;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CustomerBuilder;


/**
 * Test with database to proof core order process functionality.</br>
 * <i>This Test is explicity without Transactional annotation</i>
 * 
 * @author Marek Switajski
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class OrderServiceTest {

	@Autowired CustomerServiceImpl customerService;
	@Autowired ReportItemServiceImpl itemService;
	@Autowired CatalogProductServiceImpl productService;
	@Autowired OrderServiceImpl orderService;
	@Autowired OrderItemRepository itemRepo;

	private static final int QUANTITY_INITIAL = 4;
	private static final int QUANTITY_IN_NEXT_STATE = 2;
	private static final Amount AMOUNT = new Amount(new BigDecimal("23.34"), Currency.EUR);
	private static final Address INVOICE_ADDRESS = AddressBuilder.buildWithGeneratedAttributes(12);

	@Test
	public void shouldOrder(){
		OrderItem item = createItemAndOrder(QUANTITY_INITIAL, 3);
		
		assertNotNull(item);
		assertNotNull(item.getId());
		assertNotNull(item.getProduct());
		
		assertNotNull(item.getOrder().getId());
	}

	@Test
	public void shouldConfirm(){
		OrderItem item = createItemAndOrder(QUANTITY_INITIAL, 4);
		
		assertFalse(new ConfirmedSpecification(false, false).isSatisfiedBy(item));
		
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		map.put(item.getId(), 4);
		ConfirmationReport cr = orderService.confirm("13245jjj", item.getOrder().getOrderNumber(), new Date(), map);
		
		OrderItem oi = cr.getEvents().iterator().next().getOrderItem();
		
		assertTrue(new ConfirmedSpecification(false, false).isSatisfiedBy(oi));
		assertNotNull("Primary key of ConfirmationReport is null after persist", cr.getId());
		assertNotNull("Primary key of OrderItem is null after persist", oi.getId());
		assertNotNull("Primary key of FlexibleOrder is null after persist", oi.getOrder().getId());
		assertTrue(cr != null);
		assertTrue(cr.getEvents().size() == 1);
		assertTrue(cr.getEvents().iterator().next().getType() == HandlingEventType.CONFIRM);
		
		// assert events are as expected
		Set<HandlingEvent> confirmedEvents = item.getAllHesOfType(HandlingEventType.CONFIRM);
		assertEquals(1, confirmedEvents.size());
		ConfirmationReport orderConfirmation = confirmedEvents.iterator().next().getOrderConfirmation();
		assertNotNull(orderConfirmation.getDocumentNumber());
		assertNotNull(orderConfirmation.getInvoiceAddress());
		assertNotNull(orderConfirmation.getShippingAddress());
	}
	
	@Test
	public void shouldDeliver(){
		OrderItem item = createItemAndOrder(QUANTITY_INITIAL, 4);
		
		Map<Long, Integer> ois = new HashMap<Long, Integer>();
		ois.put(item.getId(), 4);
		ConfirmationReport cr = orderService.confirm("13245jjj", 
				item.getOrder().getOrderNumber(), new Date(), ois);
		
		Map<Long, Integer> hes = new HashMap<Long, Integer>();
		hes.put(cr.getEvents().iterator().next().getId(), 4);
		DeliveryNotes dn = orderService.deliver("invoiceNumber1", "123", "jk-kj", INVOICE_ADDRESS, hes);
		
		assertFalse(dn.getEvents().isEmpty());
		assertTrue(dn.getEvents().iterator().next().getType() == HandlingEventType.SHIP);
	}
	
	private OrderItem createItemAndOrder(int quantity, Integer random){
		Customer customer = customerService.create(
				CustomerBuilder.buildWithGeneratedAttributes(random));
		CatalogProduct product = productService.create(
				CatalogProductBuilder.buildWithGeneratedAttributes(random));

		return orderService.order(customer, random.toString(), 
				product.toProduct(), quantity, new Amount(BigDecimal.TEN, Currency.EUR));
	}
	

//	@Test
//	public void shouldDeconfirm(){
//		OrderItem item = createItemAndOrder(10, 11);
//		item = deliver(item, 10, "9087");
//		
//		assertTrue(new ConfirmedSpecification(false, false).isSatisfiedBy(item));
//		item = orderService.deconfirm(item);
//		
//		assertFalse(new ConfirmedSpecification(false, false).isSatisfiedBy(item));
//	}
//
//
//	@Test
//	public void shouldwithdrawInvoiceItemAndShipment(){
//		OrderItem item = createItemAndOrder(10, 17654);
//		item = deliver(item, 5, "Oasdf");
//		
//		String invoiceNo = "invoiceno";
//		item = orderService.shipAndInvoice(item, 5, invoiceNo, null, 
//				AddressBuilder.buildWithGeneratedAttributes(12));
//		assertTrue(new ShippedSpecification(false, false).isSatisfiedBy(item));
//		
//		item = orderService.withdrawInvoiceItemAndShipment(item);
//		assertFalse(new ShippedSpecification(false, false).isSatisfiedBy(item));
//	}
//
//	
//	@Test(expected = IllegalArgumentException.class)
//	public void shouldRejectOrderConfirmation(){
//		OrderItem item = createItemAndOrder(10, 15);
//		deliver(item, 51, "tempOrderConfirmationNumber");
//	}
//	
//	@Test(expected = IllegalArgumentException.class)
//	public void shouldRejectInvoicing(){
//		OrderItem item = createItemAndOrder(1411, 112345);
//		deliver(item, 5, "tempOrderConfirmationNumber");
//		
//		orderService.shipAndInvoice(item, 10, "invoiceNomnasdf", null, AddressBuilder.buildWithGeneratedAttributes(67766767));
//	}
//	
//	@Test
//	public void shouldReceivePayment(){
//		OrderItem item = createItemAndOrder(10, 14);
//		String orderConfirmationNo = "1235";
//		item = deliver(item, 5, orderConfirmationNo);
//		
//		orderService.receivePayment(orderConfirmationNo, new Date());
//	}

}
