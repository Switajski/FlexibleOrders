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
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.web.entities.ItemDto;


/**
 * Test with database to proof core order process functionality.</br>
 * 
 * @author Marek Switajski
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
public class OrderServiceIntegrationTest {

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
		
		Order order = createOrder(customer, QUANTITY_INITIAL, 3);
		
		assertNotNull(order.getId());
		for (OrderItem item : order.getItems()){
			assertNotNull(item);
			assertNotNull(item.getId());
			assertNotNull(item.getProduct());
			assertNotNull(item.getOrder().getId());
		};
		
		Page<ItemDto> risToBeConfirmed = itemService.retrieveAllToBeConfirmedByCustomer(customer, createPageRequest());
		assertTrue(risToBeConfirmed != null);
		assertTrue(risToBeConfirmed.getTotalElements() != 0l);
	}

	@Rollback
	@Test
	public void shouldConfirm(){
		setUp();
		Integer orderNumber = 4;
		Customer customer = persistentCustomers.get(1);
		
		Order order = createOrder(customer, QUANTITY_INITIAL, orderNumber);
		
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), 
				"AB-4", 
				new Date(), 
				createReportItemsToConfirm(order));
		
		assertConfirmed(cr, customer);
	}

	private void assertConfirmed(ConfirmationReport cr, Customer customer) {
		assertTrue(cr != null);
		assertTrue(cr.getItems().size() == 2);
		assertTrue(cr.getItems().iterator().next().getType() == ReportItemType.CONFIRM);
		
		// assert events are as expected
		for (ReportItem he : cr.getItems()){
			assertTrue(new ConfirmedSpecification(false, false).isSatisfiedBy(he.getOrderItem()));
			assertNotNull("Primary key of ConfirmationReport is null after persist", cr.getId());
			assertNotNull("Primary key of OrderItem is null after persist", he.getOrderItem().getId());
			assertNotNull("Primary key of FlexibleOrder is null after persist", he.getOrderItem().getOrder().getId());
			
			Set<ReportItem> confirmedEvents = he.getOrderItem().getAllHesOfType(ReportItemType.CONFIRM);
			assertEquals(1, confirmedEvents.size());
			ConfirmationReport orderConfirmation = confirmedEvents.iterator().next().getOrderConfirmation();
			assertNotNull(orderConfirmation.getDocumentNumber());
			assertNotNull(orderConfirmation.getInvoiceAddress());
			assertNotNull(orderConfirmation.getShippingAddress());
		}
		
		Page<ItemDto> risToBeShipped = itemService.retrieveAllToBeShipped(customer, createPageRequest());
		assertTrue(risToBeShipped != null);
		assertTrue(risToBeShipped.getTotalElements() != 0l);
	}

	private List<ItemDto> createReportItemsToConfirm(Order order) {
		List<ItemDto> ris = new ArrayList<ItemDto>();
		for (OrderItem item:order.getItems()){
			assertFalse(new ConfirmedSpecification(false, false).isSatisfiedBy(item));
			ItemDto ri = new ItemDto();
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
		
		Order order = createOrder(customer, QUANTITY_INITIAL, orderNumber);
		
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
		Order order = createOrder(customer, QUANTITY_INITIAL, orderNumber);
		
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
		Order order;
		Page<ItemDto> risToBeInvoiced = 
				itemService.retrieveAllToBeInvoiced(customer, createPageRequest());
		assertTrue("deleted DeliveryNotes should not be part of report items to be invoiced", 
				risToBeInvoiced.getContent().isEmpty());
		
		Page<ItemDto> risToBeShipped = 
				itemService.retrieveAllToBeShipped(customer, createPageRequest());
		assertTrue("after deletion of DeliveryNotes, ReportItems should be shippable again", 
				!risToBeShipped.getContent().isEmpty());
		
		order = itemService.retrieveOrder(risToBeShipped.getContent().get(0).getOrderNumber());
		for (OrderItem oi:order.getItems())
			assertTrue("Shipping costs should have been deleted",
					oi.getProduct().getProductType() != ProductType.SHIPPING);
	}
	
	private void assertDeliveryNotesAsExpected(DeliveryNotes dn, Customer customer) {
		assertTrue(dn.getId() != null);
		assertFalse(dn.getItems().isEmpty());
		for (ReportItem he : dn.getItems()){
			assertTrue(he.getType() == ReportItemType.SHIP);
			assertTrue(he.getId() != null);
			assertTrue(he.getDeliveryNotes().getId() != null);
			assertTrue(he.getOrderConfirmation().getId() != null);
			assertTrue(he.getQuantity() > 0);
		}
		
		Page<ItemDto> risToBePaid = itemService.retrieveAllToBeInvoiced(customer, createPageRequest());
		assertTrue(risToBePaid != null);
		assertTrue(risToBePaid.getTotalElements() != 0l);
	}
	
	@Rollback
	@Test
	public void shouldDeliverPartially(){
		setUp();
		Integer orderNumber = 122;
		Customer customer = persistentCustomers.get(6);
		
		Order order = createOrder(customer, 10, orderNumber);
		
		ConfirmationReport cr = orderService.confirm(
				order.getOrderNumber(), 
				"AB-".concat(orderNumber.toString()), 
				new Date(), 
				createReportItemsToConfirm(order));
		
		List<ItemDto> risToShip = createReportItems(cr);
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

	private List<ItemDto> reduceQuantity(List<ItemDto> risToShip, int reduceBy) {
		for (ItemDto ri : risToShip){
			ri.setQuantityLeft(ri.getQuantityLeft() - reduceBy);
		}
		return risToShip;
	}
	
	private void assertPartialDeliveryNotesAsExpected(Customer customer, DeliveryNotes dn, ConfirmationReport cr) {
		Page<ItemDto> risToBeShipped =  itemService.retrieveAllToBeShipped(createPageRequest());
		Page<ItemDto> risToBeInvoiced =  itemService.retrieveAllToBeInvoiced(createPageRequest());
		
		assertTrue("should still find items to be shipped", risToBeShipped.getTotalElements() != 0l);
		assertTrue("should find shipped items", risToBeInvoiced.getTotalElements() != 0l);
		
		for (ItemDto ri : risToBeShipped)
			assertEquals("shipped quantity of item is false", new Integer(10), ri.getQuantity());
		
		for (ItemDto ri : risToBeInvoiced)
			assertEquals("paid quantity of item is false", new Integer(7), ri.getQuantity());
	}

	private PageRequest createPageRequest() {
		return new PageRequest(0, 1);
	}

	private Order createOrder(Customer customer, int quantity, Integer random){
		
		ItemDto r1 = new ItemDto();
		r1.setProduct(persistentProducts.get(0).getProductNumber());
		r1.setQuantity(quantity);
		
		ItemDto r2 = new ItemDto();
		r2.setProduct(persistentProducts.get(0).getProductNumber());
		r2.setQuantity(quantity);
		
		List<ItemDto> ris = new ArrayList<ItemDto>();
		ris.add(r1); ris.add(r2);

		return orderService.order(customer.getId(), random.toString(), ris);
	}
	
	@Rollback
	@Test
	public void shouldMarkAsPayed(){
		setUp();
		Integer orderNumber = 545;
		Order order = createOrder(persistentCustomers.get(6), QUANTITY_INITIAL, orderNumber);
		
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
		
		List<ItemDto> risToComplete = createReportItems(invoice);
		Receipt receipt = orderService.markAsPayed(dn.getDocumentNumber(), "billoreceipt", new Date(), 
				risToComplete);
		
		assertReceiptAsExpected(receipt);
		
		
		Page<ItemDto> risCompleted = itemService.retrieveAllCompleted(createPageRequest());
		assertTrue(risCompleted != null);
		assertTrue(risCompleted.getTotalElements() != 0l);
	}

	private void assertReceiptAsExpected(Receipt receipt) {
		assertTrue(receipt.getId() != null);
		assertFalse(receipt.getItems().isEmpty());
		for (ReportItem he : receipt.getItems()){
			assertTrue(he.getType() == ReportItemType.PAID);
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
		Order order = createOrder(persistentCustomers.get(4), 10, orderNumber);
		
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
		
		for (ReportItem he : invoice.getItems()){
			assertTrue(he.getId() != null);
			assertTrue(he.getType() == ReportItemType.INVOICE);
			assertTrue(he.getQuantity() > 0);
		}
	}

	private List<ItemDto> createReportItems(Report dn) {
		List<ItemDto> ris = new ArrayList<ItemDto>();
		for (ReportItem he : dn.getItems()){
			ItemDto ri = new ItemDto();
			ri.setId(he.getId());
			ri.setQuantityLeft(he.getQuantity());
			ris.add(ri);
		}
		return ris;
	}
	
}
