package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.PayedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.repository.CategoryRepository;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CustomerBuilder;


@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class TransitionServiceTest {

	@Autowired CustomerService customerService;
	@Autowired ItemServiceImpl itemService;
	@Autowired ItemRepository itemRepo;
	@Autowired CatalogProductRepository productService;
	@Autowired HandlingEventService transitionService;
	@Autowired CategoryRepository categoryService;

	private final static Long PRODUCT_NR = 98134756l;
	private final static int CUSTOMER_NR = 1234;
	private static final int QUANTITY_INITIAL = 4;
	private static final int QUANTITY_IN_NEXT_STATE = 2;
	private static final Long ORDER_NR = 1976578l;
	private static final long ORDER_CONFIRMATION_NR = 98756345l;
	private static final long INVOICE_NR = 785439987l;
	private static final Long ACCOUNT_NR = 13794058l;


	@Transactional
	@Rollback
	@Test
	public void shouldConfirmOrderItem(){
		Item oi = createGivenOrderItem();
		itemRepo.saveAndFlush(oi);

		Item si = transitionService.confirm(oi,
				new ConfirmedSpecification(
						false, ORDER_CONFIRMATION_NR));

		assertConfirmedState(oi, si);
	}

	@Ignore("Feature disabled - order items can just be confirmed completely")
	@Transactional
	@Rollback
	@Test
	public void shouldConfirmOrderItemPartially(){
		OrderItem oi = createGivenOrderItem();

		ShippingItem si = transitionService.confirm(oi,
				new ConfirmationSpecification(
						false, ORDER_CONFIRMATION_NR));

		assertPartialConfirmedState(oi, si);
	}

	@Transactional
	@Rollback
	@Test
	public void shouldDeconfirmShippingItem(){
		OrderItem oi = createGivenOrderItem();

		ShippingItem si = transitionService.confirm(oi,
				new ConfirmationSpecification(
						false, ORDER_CONFIRMATION_NR));

		si = transitionService.deconfirm(si);

		assertDeconfirmedState(oi, si);
	}

	@Transactional
	@Rollback
	@Test
	public void shouldDeliverShippingItemPartially(){
		ShippingItem si = createGivenShippingItem();

		InvoiceItem ii = transitionService.deliver(
				si,
				new ShippedSpecification(
						QUANTITY_IN_NEXT_STATE,
						INVOICE_NR,
						si.getShippingAddress()
						)
				);
		assertPartiallyShippedState(si, ii);
	}
	
	private void assertPartiallyShippedState(ShippingItem si, InvoiceItem ii) {
		assertEquals("Quantity left of shipping item wrong", 
				new Integer(QUANTITY_INITIAL-QUANTITY_IN_NEXT_STATE), 
				si.getQuantityLeft());
		assertEquals("Quantity of invoice item wrong",QUANTITY_IN_NEXT_STATE,
				ii.getQuantity());
		assertEquals("Quantity left of invoice item wrong",
				ii.getQuantity(), ii.getQuantityLeft());
		
	}

	@Transactional
	@Rollback
	@Test
	public void shouldDeliverShippingItem(){
		ShippingItem si = createGivenShippingItem();

		InvoiceItem ii = transitionService.deliver(
				si,
				new ShippedSpecification(
						QUANTITY_INITIAL,
						INVOICE_NR,
						si.getShippingAddress()
						)
				);

		assertShippedState(si, ii);
	}

	@Transactional
	@Rollback
	@Test
	public void shouldWithdrawShippingItem(){
		ShippingItem si = createGivenShippingItem();

		InvoiceItem ii = transitionService.deliver(
				si,
				new ShippedSpecification(
						QUANTITY_INITIAL,
						INVOICE_NR,
						si.getShippingAddress()
						)
				);

		ii = transitionService.withdraw(ii);

		assertWithdrawnState(si, ii);
	}

	@Transactional
	@Rollback
	@Test
	public void shouldWithdrawShippingItemPartially(){
		ShippingItem si = createGivenShippingItem();

		InvoiceItem ii = transitionService.deliver(
				si,
				new ShippedSpecification(
						QUANTITY_IN_NEXT_STATE,
						INVOICE_NR,
						si.getShippingAddress()
						)
				);

		transitionService.deliver(
				si,
				new ShippedSpecification(
						QUANTITY_IN_NEXT_STATE,
						INVOICE_NR,
						si.getShippingAddress()
						)
				);

		ii = transitionService.withdraw(ii);

		assertPartialWithdrawnState(si, ii);
	}

	@Transactional
	@Rollback
	@Test
	public void shouldCompleteInvoiceItem(){
		InvoiceItem ii = getGivenInvoiceItem();

		ArchiveItem ai = transitionService.complete(ii, new PayedSpecification(ACCOUNT_NR));

		assertCompletedState(ii, ai);
	}

	@Transactional
	@Rollback
	@Test
	public void shouldDecompleteArchiveItem(){
		InvoiceItem ii = getGivenInvoiceItem();

		ArchiveItem ai = transitionService.complete(ii, new PayedSpecification(ACCOUNT_NR));

		ai = transitionService.decomplete(ai);

		assertDecompletedState(ii, ai);
	}


	private void assertDecompletedState(InvoiceItem ii, ArchiveItem ai) {
		assertEquals("decompleted archiveItem should not be found", null, archiveItemRepository.findOne(ai.getId()));
		
		List<InvoiceItem> invoiceItems = invoiceItemRepository.findByInvoiceNumberAndProduct(ii.getInvoiceNumber(), ii.getProduct());
		assertEquals("should find only one invoice item with given product and InvoiceNumber",
				1, invoiceItems.size());
		InvoiceItem invoiceItem = invoiceItems.get(0);
		assertEquals("Invoice Number changed", ii.getOrderConfirmationNumber(), invoiceItem.getOrderConfirmationNumber());
		assertTrue("should not find withdrawn invoice item", archiveItemRepository.findByAccountNumber(ACCOUNT_NR).isEmpty());

		assertEquals("Account number of invoice item should not be set",  null, invoiceItem.getAccountNumber());
		assertEquals("quantityLeft should be added back to invoice item", QUANTITY_INITIAL, invoiceItem.getQuantityLeft());
		assertEquals("Status wrong", invoiceItem.getStatus(), Status.SHIPPED);


	}

	private void assertCompletedState(InvoiceItem ii, ArchiveItem ai) {
		Status statusShouldBe = Status.COMPLETED;

		//assert given invoice item State
		List<InvoiceItem> iis = invoiceItemRepository.findByInvoiceNumberAndProduct(INVOICE_NR, ii.getProduct());
		assertEquals("should find only one shippingItem with given Product and OrderConfirmationNumber", 1, iis.size());
		InvoiceItem invoiceItem = iis.get(0);

		assertEquals("invoice item's quantity left wrong", 0, invoiceItem.getQuantityLeft());
		assertEquals("invoice item's quantity wrong "+QUANTITY_INITIAL, QUANTITY_INITIAL, invoiceItem.getQuantity());
		assertEquals("invoice item's Status wrong",  statusShouldBe, invoiceItem.getStatus());

		//assert given invoice item State
		List<ArchiveItem> ais = archiveItemRepository.findByAccountNumber(ACCOUNT_NR);
		assertFalse(ais.isEmpty());
		ArchiveItem archiveItem = ais.get(0);

		assertEquals("archive item's quantity Left wrong", QUANTITY_INITIAL, archiveItem.getQuantityLeft());
		assertEquals("archive item's quantity wrong", QUANTITY_INITIAL, archiveItem.getQuantity());
		assertEquals("archive item's Status wrong", statusShouldBe, archiveItem.getStatus());

	}

	private void assertWithdrawnState(ShippingItem si, InvoiceItem ii) {
		List<ShippingItem> shippingItems = shippingItemRepository.findByOrderConfirmationNumberAndProduct(si.getOrderConfirmationNumber(), si.getProduct());
		assertEquals("should find only one shipping item with given product and orderConfirmationNumber",
				1, shippingItems.size());
		ShippingItem shippingItem = shippingItems.get(0);
		assertEquals("OrderConfirmationNumber changed", si.getOrderConfirmationNumber(), shippingItem.getOrderConfirmationNumber());
		assertTrue("should not find withdrawn invoice item", invoiceItemRepository.findByInvoiceNumber(INVOICE_NR).isEmpty());

		assertEquals("invoice number of shipping item should not be set",  null, shippingItem.getInvoiceNumber());
		assertEquals("quantityLeft should be added back to shipping item", new Integer(QUANTITY_INITIAL), shippingItem.getQuantityLeft());
		assertEquals("Status not set to ORDERED", shippingItem.getStatus(), Status.CONFIRMED);

	}

	private void assertPartialWithdrawnState(ShippingItem si, InvoiceItem ii) {
		List<ShippingItem> shippingItems = shippingItemRepository.findByOrderConfirmationNumberAndProduct(si.getOrderConfirmationNumber(), si.getProduct());
		assertEquals("should find only one shipping item with given product and orderConfirmationNumber",
				1, shippingItems.size());
		ShippingItem shippingItem = shippingItems.get(0);
		assertEquals("OrderConfirmationNumber changed", si.getOrderConfirmationNumber(), shippingItem.getOrderConfirmationNumber());
		assertNull("should not find withdrawn invoice item", invoiceItemRepository.findOne(ii.getId()));
		assertFalse("should find other invoice item", invoiceItemRepository.findByInvoiceNumber(ii.getInvoiceNumber()).isEmpty());

		assertNotNull("invoice number of shipping item should be set", shippingItem.getInvoiceNumber());
		assertEquals("quantityLeft should be added back to shipping item", new Integer(QUANTITY_INITIAL-QUANTITY_IN_NEXT_STATE), shippingItem.getQuantityLeft());
		assertEquals("Status should be CONFIRMED", shippingItem.getStatus(), Status.CONFIRMED);

	}

	private void assertShippedState(ShippingItem si, InvoiceItem ii) {
		Status statusShouldBe = Status.SHIPPED;

		//assert given invoice item State
		List<ShippingItem> sis = shippingItemRepository.findByOrderConfirmationNumberAndProduct(ORDER_CONFIRMATION_NR, si.getProduct());
		assertEquals("should find only one shippingItem with given Product and OrderConfirmationNumber", 1, sis.size());
		ShippingItem shippingItem = sis.get(0);

		assertEquals("shipping item's quantity left should be 0", new Integer(0), shippingItem.getQuantityLeft());
		assertEquals("shipping item's quantity should be "+QUANTITY_INITIAL, QUANTITY_INITIAL, shippingItem.getQuantity());
		assertEquals("shipping item's Status should be SHIPPED",  statusShouldBe, shippingItem.getStatus());

		//assert given invoice item State
		List<Item> iis = itemRepo.findByInvoiceNumber(INVOICE_NR);
		assertFalse(iis.isEmpty());
		Item invoiceItem = iis.get(0);

		assertEquals("invoice item's quantity wrong", QUANTITY_INITIAL, invoiceItem.getQuantityLeft());
		assertEquals("invoice item's quantity wrong", QUANTITY_INITIAL, invoiceItem.getQuantity());
		assertEquals("invoice item's Status wrong", statusShouldBe, invoiceItem.getStatus());
	}

	private Item createGivenShippingItem() { 
		Item orderItem = createGivenOrderItem();

		Item si = transitionService.confirm(
					orderItem,
					new ConfirmationSpecification(false,
							ORDER_CONFIRMATION_NR)
				);

		return si;
	}

	private Item getGivenInvoiceItem() { 
		Item si = createGivenShippingItem();

		Item ii = transitionService.deliver(
				si, new ShippedSpecification(QUANTITY_INITIAL, INVOICE_NR, si.getShippingAddress()));
				
		return ii;
	}

	private void assertDeconfirmedState(Item oi, Item si) {
		// assert proper order item state
		List<Item> orderItems = itemRepo.findByOrderNumberAndProduct(ORDER_NR, oi.getProduct());
		assertEquals("should find only one orderItem with given product and OrderNumber", 1, orderItems.size());
		Item oiIs = orderItems.get(0);
		assertEquals("Status not set to ORDERED", oiIs.getStatus(), Status.ORDERED);
		assertEquals("OrderNumber changed", oi.getOrderNumber(), oiIs.getOrderNumber());
		assertEquals("orderConfirmation number of a deconfirmed orderItem should be null", null, oi.getOrderConfirmationNumber());
		assertTrue(oiIs.getQuantityLeft() == QUANTITY_INITIAL);

		// assert proper shipping item state
		List<Item> siItems = shippingItemRepository.findByOrderConfirmationNumber(ORDER_CONFIRMATION_NR);
		assertEquals("should not find shippingItem with given orderConfirmationNumber and productNumber", siItems.isEmpty(), true);

	}

	private void assertConfirmedState(Item oi, Item si) {
		// assert proper order item state
		List<Item> orderItems = itemRepo.findByOrderNumberAndProductNumber(ORDER_NR, oi.getProductNumber());
		assertEquals("should find only one order item with given product and orderNumber", 1,  orderItems.size());
		Item equivalentOi = orderItems.get(0);
		assertEquals("Status not set to CONFIRMED", equivalentOi.getStatus(), Status.CONFIRMED);
		assertEquals("OrderNumber of orderItem should be same as given", oi.getOrderNumber(), equivalentOi.getOrderNumber());
		assertEquals("OrderConfirmation of orderItem should be same as given", oi.getOrderConfirmationNumber(), equivalentOi.getOrderConfirmationNumber());
		assertEquals(oi.getQuantity(), QUANTITY_INITIAL);
		assertEquals((oi.getQuantityLeft() == 0), true);

		// assert proper shipping item state
		List<Item> siItems = shippingItemRepository.findByOrderConfirmationNumber(ORDER_CONFIRMATION_NR);
		assertFalse("no shipping item with given orderConfirmationNumber found!", siItems.isEmpty());
		assertEquals("shipping item in repository should be the same as the one returned in OrderItem.confirm()", si, siItems.get(0));
	}

	private void assertPartialConfirmedState(Item oi, Item si){
		// assert proper order item state
		List<Item> orderItems = itemRepo.findByOrderNumberAndProduct(ORDER_NR, oi.getProduct());
		assertFalse("no order item with given orderNumber found!", orderItems.isEmpty());
		Item equivalentOi = orderItems.get(0);

		// Status should not be the next one. Quantity Left should be not null
		assertEquals("Status should be only confirmed when quantity_left equals quantity", 
				equivalentOi.getStatus(), Status.ORDERED);
		assertEquals("Quantities left and initial should be after partial confirmation deducted", (oi.getQuantityLeft() == (QUANTITY_INITIAL - QUANTITY_IN_NEXT_STATE)), true);

	}

	private Item createGivenOrderItem() {
		CatalogProduct product = CatalogProductBuilder.buildWithGeneratedAttributes(Integer.valueOf(PRODUCT_NR.toString()));
		product.setPriceNet(BigDecimal.TEN);
		product.setProductNumber(PRODUCT_NR);
		categoryService.save(product.getCategory());
		productService.saveProduct(product);

		Customer customer = CustomerBuilder.buildWithGeneratedAttributes(CUSTOMER_NR);
		customerService.save(customer);
		
		Item oi = new Item(new DeliverySpecification(product, customer, QUANTITY_INITIAL, ORDER_NR, null)); 
		itemRepo.saveAndFlush(oi);
		return oi;
	}

}
