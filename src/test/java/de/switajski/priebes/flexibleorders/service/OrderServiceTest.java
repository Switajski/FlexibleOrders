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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
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
@Transactional
public class OrderServiceTest {

	@Autowired CustomerServiceImpl customerService;
	@Autowired ReportItemServiceImpl itemService;
	@Autowired CatalogProductServiceImpl productService;
	@Autowired OrderServiceImpl orderService;
	@Autowired OrderItemRepository itemRepo;

	private static final int QUANTITY_INITIAL = 4;
	private static final Address INVOICE_ADDRESS = AddressBuilder.buildWithGeneratedAttributes(12);

	private static final Long CUSTOMER_NR_1 = 1423464l;
	private static final Long PRODUCT_NR_1 = 1222345l;
	private static final Long PRODUCT_NR_2 = 23122344l;
	
	private Customer customer;
	private CatalogProduct product1;
	private CatalogProduct product2;
	
	@Before
	public void setUp(){
		customer = customerService.create(
				CustomerBuilder.buildWithGeneratedAttributes(new Integer(CUSTOMER_NR_1.toString())));
		product1 = productService.create(
				CatalogProductBuilder.buildWithGeneratedAttributes(new Integer(PRODUCT_NR_1.toString())));
		product2 = productService.create(
				CatalogProductBuilder.buildWithGeneratedAttributes(new Integer(PRODUCT_NR_2.toString())));
	}
	
	@Rollback
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
		
		Page<ReportItem> risToBeConfirmed = itemService.retrieveAllToBeConfirmed(createPageRequest(), true);
		assertTrue(risToBeConfirmed != null);
		assertTrue(risToBeConfirmed.getTotalElements() != 0l);
	}

	@Rollback
	@Test
	public void shouldConfirm(){
		
		Integer orderNumber = 4;
		FlexibleOrder order = createOrder(QUANTITY_INITIAL, orderNumber);
		
		List<ReportItem> ris = createReportItemsToConfirm(order);
		
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), "AB-4", new Date(), ris);
		
		assertConfirmationReport(cr);
		
		Page<ReportItem> risToBeShipped = itemService.retrieveAllToBeShipped(createPageRequest(), true);
		assertTrue(risToBeShipped != null);
		assertTrue(risToBeShipped.getTotalElements() != 0l);
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
		
		List<ReportItem> risToShip = createReportItems(cr);
		DeliveryNotes dn = orderService.deliver("R-".concat(orderNumber.toString()),
				"trackNumber", "packNo", INVOICE_ADDRESS, null, risToShip);
		
		assertDeliveryNotesAsExpected(dn);
		
		Page<ReportItem> risToBePaid = itemService.retrieveAllToBeInvoiced(createPageRequest(), true);
		assertTrue(risToBePaid != null);
		assertTrue(risToBePaid.getTotalElements() != 0l);
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
	
	@Rollback
	@Test
	public void shouldDeliverPartially(){
		
		Integer orderNumber = 5;
		FlexibleOrder order = createOrder(10, orderNumber);
		
		List<ReportItem> risToConfirm = createReportItemsToConfirm(order);
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), "AB-".concat(orderNumber.toString()), new Date(), risToConfirm);
		
		List<ReportItem> risToShip = createReportItems(cr);
		reduceQuantity(risToShip, 3);
		DeliveryNotes dn = orderService.deliver("R-".concat(orderNumber.toString()),
				"trackNumber", "packNo", INVOICE_ADDRESS, null, risToShip);
		
		assertPartialDeliveryNotesAsExpected(dn, cr);
	}

	private void reduceQuantity(List<ReportItem> risToShip, int reduceBy) {
		for (ReportItem ri : risToShip)
			ri.setQuantity(ri.getQuantity() - reduceBy);
	}
	
	private void assertPartialDeliveryNotesAsExpected(DeliveryNotes dn, ConfirmationReport cr) {
		Page<ReportItem> risToBeShipped =  itemService.retrieveAllToBeShipped(createPageRequest(), true);
		Page<ReportItem> risToBePaid =  itemService.retrieveAllToBeInvoiced(createPageRequest(), true);
		
		assertTrue("should still find items to be shipped", risToBeShipped.getTotalElements() != 0l);
		assertTrue("should find shipped items", risToBePaid.getTotalElements() != 0l);
		
		for (ReportItem ri : risToBeShipped)
			assertEquals("shipped quantity of item is false", new Integer(10), ri.getQuantity());
		
		for (ReportItem ri : risToBePaid)
			assertEquals("paid quantity of item is false", new Integer(7), ri.getQuantity());
	}

	private PageRequest createPageRequest() {
		return new PageRequest(0, 1);
	}

	private FlexibleOrder createOrder(int quantity, Integer random){
		
		ReportItem r1 = new ReportItem();
		r1.setProduct(product1.getProductNumber());
		r1.setQuantity(quantity);
		
		ReportItem r2 = new ReportItem();
		r2.setProduct(product2.getProductNumber());
		r2.setQuantity(quantity);
		
		List<ReportItem> ris = new ArrayList<ReportItem>();
		ris.add(r1); ris.add(r2);

		return orderService.order(customer.getId(), random.toString(), ris);
	}
	
	@Rollback
	@Test
	public void shouldComplete(){
		
		Integer orderNumber = 5;
		FlexibleOrder order = createOrder(QUANTITY_INITIAL, orderNumber);
		
		List<ReportItem> risToConfirm = createReportItemsToConfirm(order);
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), "AB-".concat(orderNumber.toString()), new Date(), risToConfirm);
		
		List<ReportItem> risToShip = createReportItems(cr);
		DeliveryNotes dn = orderService.deliver("R-".concat(orderNumber.toString()),
				"trackNumber", "packNo", INVOICE_ADDRESS, null, risToShip);
		
		List<ReportItem> risToComplete = createReportItems(dn);
		Receipt receipt = orderService.markAsPayed(dn.getDocumentNumber(), "billoreceipt", new Date(), 
				risToComplete);
		
		assertReceiptAsExpected(receipt);
		
		
		Page<ReportItem> risCompleted = itemService.retrieveAllCompleted(createPageRequest(), true);
		assertTrue(risCompleted != null);
		assertTrue(risCompleted.getTotalElements() != 0l);
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

	@Test
	public void shouldInvoice(){
		Integer orderNumber = 5;
		FlexibleOrder order = createOrder(10, orderNumber);
		
		List<ReportItem> risToConfirm = createReportItemsToConfirm(order);
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), "AB-".concat(orderNumber.toString()), new Date(), risToConfirm);
		
		List<ReportItem> risToShip = createReportItems(cr);
		DeliveryNotes dn = orderService.deliver("L-".concat(orderNumber.toString()),
				"trackNumber", "packNo", INVOICE_ADDRESS, null, risToShip);
		
		List<ReportItem> risToInvoice = createReportItems(dn);
		Invoice i = orderService.invoice("R-".concat(orderNumber.toString()), "paymentCondition", 
				INVOICE_ADDRESS, risToInvoice);
	}

	private List<ReportItem> createReportItems(Report dn) {
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (HandlingEvent he : dn.getEvents()){
			ReportItem ri = new ReportItem();
			ri.setId(he.getId());
			ri.setQuantity(he.getQuantity());
			ris.add(ri);
		}
		return ris;
	}
	
}
