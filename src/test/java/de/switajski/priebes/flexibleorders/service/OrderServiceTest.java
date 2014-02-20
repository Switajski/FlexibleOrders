package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;


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
	private static final Address INVOICE_ADDRESS = AddressBuilder.buildWithGeneratedAttributes(12);

	private static final Long CUSTOMER_ID_1 = 1l;
	private static final Long PRODUCT_ID_1 = 1l;
	private static final Long PRODUCT_ID_2 = 2l;
	
	@Before
	public void setUp(){
		customerService.create(
				CustomerBuilder.buildWithGeneratedAttributes(new Integer(CUSTOMER_ID_1.toString())));
		productService.create(
				CatalogProductBuilder.buildWithGeneratedAttributes(new Integer(PRODUCT_ID_1.toString())));
		productService.create(
				CatalogProductBuilder.buildWithGeneratedAttributes(new Integer(PRODUCT_ID_2.toString())));
	}
	
	@Test
	public void shouldOrder(){
		FlexibleOrder order = createOrder(QUANTITY_INITIAL, 3);
		
		assertNotNull(order.getId());
		for (OrderItem item : order.getItems()){
			assertNotNull(item);
			assertNotNull(item.getId());
			assertNotNull(item.getProduct());
			assertNotNull(item.getOrder().getId());
		};
	}

	@Test
	public void shouldConfirm(){
		Integer orderNumber = 4;
		FlexibleOrder order = createOrder(QUANTITY_INITIAL, orderNumber);
		
		List<ReportItem> ris = createReportItemsToConfirm(order);
		
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), "AB-4", new Date(), ris);
		
		assertConfirmationReport(cr);
	}

	private void assertConfirmationReport(ConfirmationReport cr) {
		assertTrue(cr != null);
		assertTrue(cr.getEvents().size() == 2);
		assertTrue(cr.getEvents().iterator().next().getType() == HandlingEventType.CONFIRM);
		
		// assert events are as expected
		for (HandlingEvent he : cr.getEvents()){
			assertTrue(new ConfirmedSpecification(false, false).isSatisfiedBy(he.getOrderItem()));
			assertNotNull("Primary key of ConfirmationReport is null after persist", cr.getId());
			assertNotNull("Primary key of OrderItem is null after persist", he.getOrderItem().getId());
			assertNotNull("Primary key of FlexibleOrder is null after persist", he.getOrderItem().getOrder().getId());
			
			Set<HandlingEvent> confirmedEvents = he.getOrderItem().getAllHesOfType(HandlingEventType.CONFIRM);
			assertEquals(1, confirmedEvents.size());
			ConfirmationReport orderConfirmation = confirmedEvents.iterator().next().getOrderConfirmation();
			assertNotNull(orderConfirmation.getDocumentNumber());
			assertNotNull(orderConfirmation.getInvoiceAddress());
			assertNotNull(orderConfirmation.getShippingAddress());
		}
	}

	private List<ReportItem> createReportItemsToConfirm(FlexibleOrder order) {
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (OrderItem item:order.getItems()){
			assertFalse(new ConfirmedSpecification(false, false).isSatisfiedBy(item));
			ReportItem ri = new ReportItem();
			ri.setId(item.getId());
			ri.setQuantity(item.getOrderedQuantity());
			ri.setProduct(item.getProduct().getProductNumber());
			ris.add(ri);
		}
		return ris;
	}
	
	@Test
	public void shouldDeliver(){
		Integer orderNumber = 5;
		FlexibleOrder order = createOrder(QUANTITY_INITIAL, orderNumber);
		
		List<ReportItem> risToConfirm = createReportItemsToConfirm(order);
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), "AB-".concat(orderNumber.toString()), new Date(), risToConfirm);
		
		List<ReportItem> risToShip = createReportItemsToShip(cr);
		DeliveryNotes dn = orderService.deliver("R-".concat(orderNumber.toString()),
				"trackNumber", "packNo", INVOICE_ADDRESS, risToShip);
		
		assertDeliveryNotesAsExpected(dn);
	}

	private void assertDeliveryNotesAsExpected(DeliveryNotes dn) {
		assertTrue(dn.getId() != null);
		assertFalse(dn.getEvents().isEmpty());
		for (HandlingEvent he : dn.getEvents()){
			assertTrue(he.getType() == HandlingEventType.SHIP);
			assertTrue(he.getId() != null);
			assertTrue(he.getInvoice() != null);
			assertTrue(he.getInvoice().getId() != null);
			assertTrue(he.getQuantity() > 0);
		}
	}
	
	private List<ReportItem> createReportItemsToShip(ConfirmationReport cr) {
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (HandlingEvent he : cr.getEvents()){
			assertFalse(new ShippedSpecification(false, false).isSatisfiedBy(he.getOrderItem()));
			ReportItem ri = new ReportItem();
			ri.setId(he.getId());
			ri.setQuantity(he.getQuantity());
			ris.add(ri);
		}
		return ris;
	}

	private FlexibleOrder createOrder(int quantity, Integer random){
		
		ReportItem r1 = new ReportItem();
		r1.setProduct(PRODUCT_ID_1);
		r1.setQuantity(QUANTITY_INITIAL);
		
		ReportItem r2 = new ReportItem();
		r2.setProduct(PRODUCT_ID_2);
		r2.setQuantity(QUANTITY_INITIAL);
		
		List<ReportItem> ris = new ArrayList<ReportItem>();
		ris.add(r1); ris.add(r2);

		return orderService.order(CUSTOMER_ID_1, random.toString(), ris);
	}
	
	@Test
	public void shouldComplete(){
		Integer orderNumber = 5;
		FlexibleOrder order = createOrder(QUANTITY_INITIAL, orderNumber);
		
		List<ReportItem> risToConfirm = createReportItemsToConfirm(order);
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), "AB-".concat(orderNumber.toString()), new Date(), risToConfirm);
		
		List<ReportItem> risToShip = createReportItemsToShip(cr);
		DeliveryNotes dn = orderService.deliver("R-".concat(orderNumber.toString()),
				"trackNumber", "packNo", INVOICE_ADDRESS, risToShip);
		
		List<ReportItem> risToComplete = createReportItemsToComplete(dn);
		Receipt receipt = orderService.markAsPayed(dn.getDocumentNumber(), "billoreceipt", new Date(), 
				risToComplete);
		
		assertReceiptAsExpected(receipt);
	}

	private void assertReceiptAsExpected(Receipt receipt) {
		assertTrue(receipt.getId() != null);
		assertFalse(receipt.getEvents().isEmpty());
		for (HandlingEvent he : receipt.getEvents()){
			assertTrue(he.getType() == HandlingEventType.PAID);
			assertTrue(he.getId() != null);
			assertTrue(he.getReceipt() != null);
			assertTrue(he.getReceipt().getId() != null);
			assertTrue(he.getQuantity() > 0);
		}
	}

	private List<ReportItem> createReportItemsToComplete(DeliveryNotes dn) {
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (HandlingEvent he : dn.getEvents()){
			ReportItem ri = new ReportItem();
			ri.setId(he.getId());
			ri.setQuantity(he.getQuantity());
			ris.add(ri);
		}
		return ris;
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
