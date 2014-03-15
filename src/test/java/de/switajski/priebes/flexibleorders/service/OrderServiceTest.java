package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Currency;
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
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;


/**
 * Test with database to proof core order process functionality.</br>
 * 
 * @author Marek Switajski
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
public class OrderServiceTest {

	private static final int CUSTOMER_NUMBER = 10;
	private static final int PRODUCT_NUMBER = 2;
	private static final int QUANTITY_INITIAL = 4;
	private static final Address INVOICE_ADDRESS = AddressBuilder.buildWithGeneratedAttributes(12);

	@Autowired CustomerServiceImpl customerService;
	@Autowired ReportItemServiceImpl itemService;
	@Autowired CatalogProductServiceImpl productService;
	@Autowired OrderServiceImpl orderService;
	
	private List<Customer> persistentCustomers = new ArrayList<Customer>();
	private List<CatalogProduct> persistentProducts = new ArrayList<CatalogProduct>();
	
	public void setUp(){
		for (int i = 0;i<CUSTOMER_NUMBER;i++){
		persistentCustomers.add(customerService.create(
				CustomerBuilder.buildWithGeneratedAttributes(i)));
		}
		for (int i = 0;i<PRODUCT_NUMBER;i++){
		persistentProducts.add(
				productService.create(CatalogProductBuilder.buildWithGeneratedAttributes(i)));
		}
	}
	
	@Rollback
	@Test
	public void shouldOrder(){
		setUp();
		Customer customer = persistentCustomers.get(0);
		
		FlexibleOrder order = createOrder(customer, QUANTITY_INITIAL, 3);
		
		assertNotNull(order.getId());
		for (OrderItem item : order.getItems()){
			assertNotNull(item);
			assertNotNull(item.getId());
			assertNotNull(item.getProduct());
			assertNotNull(item.getOrder().getId());
		};
		
		Page<ReportItem> risToBeConfirmed = itemService.retrieveAllToBeConfirmedByCustomer(customer, createPageRequest(), true);
		assertTrue(risToBeConfirmed != null);
		assertTrue(risToBeConfirmed.getTotalElements() != 0l);
	}

	@Rollback
	@Test
	public void shouldConfirm(){
		setUp();
		Integer orderNumber = 4;
		Customer customer = persistentCustomers.get(1);
		
		FlexibleOrder order = createOrder(customer, QUANTITY_INITIAL, orderNumber);
		
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), 
				"AB-4", 
				new Date(), 
				createReportItemsToConfirm(order));
		
		assertConfirmed(cr, customer);
	}

	private void assertConfirmed(ConfirmationReport cr, Customer customer) {
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
		
		Page<ReportItem> risToBeShipped = itemService.retrieveAllToBeShipped(customer, createPageRequest(), true);
		assertTrue(risToBeShipped != null);
		assertTrue(risToBeShipped.getTotalElements() != 0l);
	}

	private List<ReportItem> createReportItemsToConfirm(FlexibleOrder order) {
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (OrderItem item:order.getItems()){
			assertFalse(new ConfirmedSpecification(false, false).isSatisfiedBy(item));
			ReportItem ri = new ReportItem();
			ri.setId(item.getId());
			ri.setQuantityLeft(item.getOrderedQuantity());
			ri.setProduct(item.getProduct().getProductNumber());
			ris.add(ri);
		}
		return ris;
	}
	
	@Test
	public void shouldDeliver(){
		setUp();
		Integer orderNumber = 5;
		Customer customer = persistentCustomers.get(4);
		
		FlexibleOrder order = createOrder(customer, QUANTITY_INITIAL, orderNumber);
		
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), 
				"AB-".concat(orderNumber.toString()), 
				new Date(), 
				createReportItemsToConfirm(order));
		
		DeliveryNotes dn = orderService.deliver(
				"R-".concat(orderNumber.toString()),
				"trackNumber", 
				"packNo", 
				INVOICE_ADDRESS, 
				null, 
				createReportItems(cr));
		
		assertDeliveryNotesAsExpected(dn, customer);
	}
	
	@Test
	public void shouldDeleteDeliveryNotes(){
		setUp();
		Integer orderNumber = 5123;
		Customer customer = persistentCustomers.get(8);
		FlexibleOrder order = createOrder(customer, QUANTITY_INITIAL, orderNumber);
		
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), 
				"AB-".concat(orderNumber.toString()), 
				new Date(), 
				createReportItemsToConfirm(order));
		DeliveryNotes dn = orderService.deliver(
				"R-".concat(orderNumber.toString()),
				"trackNumber", 
				"packNo", 
				INVOICE_ADDRESS, 
				new Amount(BigDecimal.TEN, Currency.EUR), //ShippingCosts 
				createReportItems(cr));
		assertDeliveryNotesAsExpected(dn, customer);
		
		orderService.deleteReport(dn.getDocumentNumber());
		
		assertDeliveryNotesDeleted(customer);
		
	}

	private void assertDeliveryNotesDeleted(Customer customer) {
		FlexibleOrder order;
		Page<ReportItem> risToBeInvoiced = 
				itemService.retrieveAllToBeInvoiced(customer, createPageRequest(), true);
		assertTrue("deleted DeliveryNotes should not be part of report items to be invoiced", 
				risToBeInvoiced.getContent().isEmpty());
		
		Page<ReportItem> risToBeShipped = 
				itemService.retrieveAllToBeShipped(customer, createPageRequest(), true);
		assertTrue("after deletion of DeliveryNotes, ReportItems should be shippable again", 
				!risToBeShipped.getContent().isEmpty());
		
		order = itemService.retrieveOrder(risToBeShipped.getContent().get(0).getOrderNumber());
		for (OrderItem oi:order.getItems())
			assertTrue("Shipping costs should have been deleted",
					oi.getProduct().getProductType() != ProductType.SHIPPING);
	}
	
	private void assertDeliveryNotesAsExpected(DeliveryNotes dn, Customer customer) {
		assertTrue(dn.getId() != null);
		assertFalse(dn.getEvents().isEmpty());
		for (HandlingEvent he : dn.getEvents()){
			assertTrue(he.getType() == HandlingEventType.SHIP);
			assertTrue(he.getId() != null);
			assertTrue(he.getInvoice() != null);
			assertTrue(he.getInvoice().getId() != null);
			assertTrue(he.getQuantity() > 0);
		}
		
		Page<ReportItem> risToBePaid = itemService.retrieveAllToBeInvoiced(customer, createPageRequest(), true);
		assertTrue(risToBePaid != null);
		assertTrue(risToBePaid.getTotalElements() != 0l);
	}
	
	@Rollback
	@Test
	public void shouldDeliverPartially(){
		setUp();
		Integer orderNumber = 122;
		Customer customer = persistentCustomers.get(6);
		
		FlexibleOrder order = createOrder(customer, 10, orderNumber);
		
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), 
				"AB-".concat(orderNumber.toString()), 
				new Date(), 
				createReportItemsToConfirm(order));
		
		List<ReportItem> risToShip = createReportItems(cr);
		reduceQuantity(risToShip, 3);
		DeliveryNotes dn = orderService.deliver(
				"R-".concat(orderNumber.toString()),
				"trackNumber", 
				"packNo", 
				INVOICE_ADDRESS, 
				null, 
				risToShip);
		
		assertPartialDeliveryNotesAsExpected(customer, dn, cr);
	}

	private List<ReportItem> reduceQuantity(List<ReportItem> risToShip, int reduceBy) {
		for (ReportItem ri : risToShip){
			ri.setQuantityLeft(ri.getQuantityLeft() - reduceBy);
		}
		return risToShip;
	}
	
	private void assertPartialDeliveryNotesAsExpected(Customer customer, DeliveryNotes dn, ConfirmationReport cr) {
		Page<ReportItem> risToBeShipped =  itemService.retrieveAllToBeShipped(createPageRequest(), true);
		Page<ReportItem> risToBeInvoiced =  itemService.retrieveAllToBeInvoiced(createPageRequest(), true);
		
		assertTrue("should still find items to be shipped", risToBeShipped.getTotalElements() != 0l);
		assertTrue("should find shipped items", risToBeInvoiced.getTotalElements() != 0l);
		
		for (ReportItem ri : risToBeShipped)
			assertEquals("shipped quantity of item is false", new Integer(10), ri.getQuantity());
		
		for (ReportItem ri : risToBeInvoiced)
			assertEquals("paid quantity of item is false", new Integer(7), ri.getQuantity());
	}

	private PageRequest createPageRequest() {
		return new PageRequest(0, 1);
	}

	private FlexibleOrder createOrder(Customer customer, int quantity, Integer random){
		
		ReportItem r1 = new ReportItem();
		r1.setProduct(persistentProducts.get(0).getProductNumber());
		r1.setQuantity(quantity);
		
		ReportItem r2 = new ReportItem();
		r2.setProduct(persistentProducts.get(0).getProductNumber());
		r2.setQuantity(quantity);
		
		List<ReportItem> ris = new ArrayList<ReportItem>();
		ris.add(r1); ris.add(r2);

		return orderService.order(customer.getId(), random.toString(), ris);
	}
	
	@Rollback
	@Test
	public void shouldMarkAsPayed(){
		setUp();
		Integer orderNumber = 545;
		FlexibleOrder order = createOrder(persistentCustomers.get(6), QUANTITY_INITIAL, orderNumber);
		
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), 
				"AB-".concat(orderNumber.toString()), 
				new Date(), 
				createReportItemsToConfirm(order));
		DeliveryNotes dn = orderService.deliver(
				"L-".concat(orderNumber.toString()),
				"trackNumber", 
				"packNo", 
				INVOICE_ADDRESS, 
				null, 
				createReportItems(cr));
		Invoice invoice = orderService.invoice(
				"R-".concat(orderNumber.toString()), 
				"paymentCondition", 
				INVOICE_ADDRESS, 
				createReportItems(dn));
		
		List<ReportItem> risToComplete = createReportItems(invoice);
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
		setUp();
		Integer orderNumber = 5;
		FlexibleOrder order = createOrder(persistentCustomers.get(4), 10, orderNumber);
		
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), 
				"AB-".concat(orderNumber.toString()), 
				new Date(), 
				createReportItemsToConfirm(order));
		
		DeliveryNotes dn = orderService.deliver(
				"L-".concat(orderNumber.toString()),
				"trackNumber", 
				"packNo", 
				INVOICE_ADDRESS, 
				null, 
				createReportItems(cr));
		
		Invoice invoice = orderService.invoice(
				"R-".concat(orderNumber.toString()), 
				"paymentCondition", 
				INVOICE_ADDRESS, 
				createReportItems(dn));
		
		assertInvoiceAsExpected(invoice);
		
	}

	private void assertInvoiceAsExpected(Invoice invoice) {
		assertTrue(invoice.getId() != null);
		assertTrue(invoice.getDocumentNumber() != null);
		
		for (HandlingEvent he : invoice.getEvents()){
			assertTrue(he.getId() != null);
			assertTrue(he.getType() == HandlingEventType.INVOICE);
			assertTrue(he.getQuantity() > 0);
		}
	}

	private List<ReportItem> createReportItems(Report dn) {
		List<ReportItem> ris = new ArrayList<ReportItem>();
		for (HandlingEvent he : dn.getEvents()){
			ReportItem ri = new ReportItem();
			ri.setId(he.getId());
			ri.setQuantityLeft(he.getQuantity());
			ris.add(ri);
		}
		return ris;
	}
	
}
