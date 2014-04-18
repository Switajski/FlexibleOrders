package de.switajski.priebes.flexibleorders.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;
import de.switajski.priebes.flexibleorders.testhelper.AbstractTestSpringContextTest;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;


/**
 * Test with database to proof core order process functionality.</br>
 * 
 * @author Marek Switajski
 *
 */
public class OrderServiceIntegrationTest extends AbstractTestSpringContextTest{

	private static final int PRODUCT_NUMBER = 14;
	private static final int QUANTITY_INITIAL = 4;
	private static final Address INVOICE_ADDRESS = AddressBuilder.buildWithGeneratedAttributes(12);

	@Autowired CustomerServiceImpl customerService;
	@Autowired ReportItemServiceImpl reportItemService;
	@Autowired CatalogProductServiceImpl productService;
	@Autowired OrderServiceImpl orderService;
	
	/**
	 * Test is intentionally not in one transaction
	 */
	@Test
	public void order_OrderShouldBePersistedAndConfirmable(){
		//given
		Customer customer = givenCustomer(78687);
		List<CatalogProduct> products = givenCatalogProducts();
		List<ItemDto> items = givenItemDtos(QUANTITY_INITIAL, products.get(0).toProduct());

		//when
		Order order = orderService.order(customer.getId(), "3", items);
		
		//then
		orderShouldBePersisted(order);
		orderShouldBeConfirmable(customer);
		
		//teardown
		tearDown(customer, order, products);
	}
	
	//TODO:Remove after test passes mvn test run
	@Transactional
	@Test
	public void confirm_OrderConfirmationShouldBePersistedAndShippableAndNotConfimable(){
		//given
		Customer customer = givenCustomer(38255821);
		List<CatalogProduct> products = givenCatalogProducts();
		List<ItemDto> items = givenItemDtos(QUANTITY_INITIAL, products.get(0).toProduct(), products.get(1).toProduct());
		Order order = orderService.order(customer.getId(), "3", items);

		//when
		ConfirmationReport confirmationReport = orderService.confirm(
				order.getOrderNumber(), 
				"AB-4", 
				new Date(), 
				extractItemDtos(order));
		
		//then
		assertPersisted(confirmationReport, customer);
		assertShippable(customer);
		assertNotConfirmable(customer);
		
		//tearDown
		tearDown(customer, order, products, confirmationReport);
	}
	
	private void assertNotConfirmable(Customer customer){
		Page<ItemDto> risToBeShipped = reportItemService.retrieveAllToBeConfirmedByCustomer(customer, createPageRequest());
		assertThat(risToBeShipped.getContent().isEmpty(), is(true));
	}
	
	//TODO:Remove after test passes mvn test run
	@Transactional
	@Test
	public void invoice_InvoiceShouldBePayableAndPersisted(){
		//given
		Customer customer = givenCustomer(715883956);
		List<CatalogProduct> products = givenCatalogProducts();
		List<ItemDto> items = givenItemDtos(
				QUANTITY_INITIAL, products.get(0).toProduct(), products.get(1).toProduct());

		Order order = orderService.order(customer.getId(), "3246", items);
		ConfirmationReport confirmationReport = orderService.confirm(
				order.getOrderNumber(), "AB4", new Date(), extractItemDtos(order));
		DeliveryNotes deliveryNotes = orderService.deliver("L4","trackNumber", "packNo", INVOICE_ADDRESS, null, 
				extractItemDtos(confirmationReport));

		//when
		Invoice invoice = orderService.invoice("R4", "paymentCondition", INVOICE_ADDRESS, 
				extractItemDtos(deliveryNotes));

		//then
		assertInvoiceAndItemsPersisted(invoice);
		//TODO: assertPayable

		//tearDown
		tearDown(customer, order, products, confirmationReport, deliveryNotes, invoice);
	}
	
	//TODO:Remove after test passes mvn test run
	@Transactional
	@Test
	public void deliver_DeliveryNotesShouldBeInvoicableAndNotShippableAndPersisted(){
		//given
		Customer customer = givenCustomer(9872347);
		List<CatalogProduct> products = givenCatalogProducts();
		List<ItemDto> items = givenItemDtos(QUANTITY_INITIAL, products.get(0).toProduct(), products.get(1).toProduct());
		
		Order order = orderService.order(customer.getId(), "4", items);
		ConfirmationReport confirmationReport = orderService.confirm(
				order.getOrderNumber(), "AB4", new Date(), extractItemDtos(order));
		
		assertShippable(customer);
		
		//when
		DeliveryNotes deliveryNotes = orderService.deliver(
				"R4",
				"trackNumber", 
				"packNo", 
				INVOICE_ADDRESS, 
				null, 
				extractItemDtos(confirmationReport));
		
		//then
		assertPersisted(deliveryNotes, customer);
		assertInvoicable(customer);
		assertNotShippable(deliveryNotes.getItems());
		
		//tearDown
		tearDown(customer, order, products, confirmationReport, deliveryNotes);
	}
	
	private void assertNotShippable(Set<ReportItem> notShippables){
		List<ItemDto> shipables = reportItemService.retrieveAllToBeShipped(createPageRequest()).getContent();
		assertThat(shipables.isEmpty(), is(true));
	}

	//TODO:Remove after test passes mvn test run
	@Transactional
	@Test
	public void deliver_PartiallyShippedDeliveryNotesShouldBeShippableAndInvoiceable(){
		//given
		int orderQty = 10;
		int shipQty = 2;
		
		Customer customer = givenCustomer(7125759);
		List<CatalogProduct> products = givenCatalogProducts();
		List<ItemDto> items = givenItemDtos(orderQty, products.get(0).toProduct(), products.get(1).toProduct());

		Order order = orderService.order(customer.getId(), "4", items);
		ConfirmationReport confirmationReport = orderService.confirm(
				order.getOrderNumber(), "AB4", new Date(), extractItemDtos(order));

		//when
		DeliveryNotes deliveryNotes = orderService.deliver(
				"L4",
				"trackNumber", 
				"packNo", 
				INVOICE_ADDRESS, 
				null, 
				itemDtosWithQty(shipQty, extractItemDtos(confirmationReport)));

		//then
		assertShippable();
		assertShippableQtyOfItemsIs(orderQty - shipQty);

		assertPersisted(deliveryNotes, customer);
		assertInvoicable(customer);
		assertInvoicableQtyOfItemsIs(shipQty);

		//tearDown
		tearDown(customer, order, products, confirmationReport, deliveryNotes);
	}
	
	//TODO:Remove after test passes mvn test run
	@Transactional
	@Test
	public void markAsPayed_ReceiptShouldBeCompletedAndNotInvoicableAndPersisted(){
		//given
		Customer customer = givenCustomer(781264823);
		List<CatalogProduct> products = givenCatalogProducts();
		List<ItemDto> items = givenItemDtos(
				QUANTITY_INITIAL, products.get(0).toProduct(), products.get(1).toProduct());

		Order order = orderService.order(customer.getId(), "4", items);
		ConfirmationReport confirmationReport = orderService.confirm(
				order.getOrderNumber(), "AB4", new Date(), extractItemDtos(order));
		DeliveryNotes deliveryNotes = orderService.deliver("L4","trackNumber", "packNo", INVOICE_ADDRESS, null, 
				extractItemDtos(confirmationReport));
		Invoice invoice = orderService.invoice("R4", "paymentCondition", INVOICE_ADDRESS, 
				extractItemDtos(deliveryNotes));
		
		//when
//		Receipt receipt = orderService.markAsPayed("R4", "Q4", new Date(), extractItemDtos(invoice));
		Receipt receipt = orderService.markAsPayed("R4", "Q4", new Date());

		//then
		assertPersisted(receipt);
		assertCompleted();
		assertNotInvoiceable();

		//tearDown
		tearDown(customer, order, products, confirmationReport, deliveryNotes, invoice, receipt);
	}
	
	private void assertNotInvoiceable() {
		List<ItemDto> items = reportItemService.retrieveAllToBeInvoiced(createPageRequest()).getContent();
		assertThat(items.isEmpty(), is(true));
	}

	private Customer givenCustomer(int i){
		return customerService.create(
				CustomerBuilder.buildWithGeneratedAttributes(i));
	}

	private List<CatalogProduct> givenCatalogProducts() {
		List<CatalogProduct> persistentProducts = new ArrayList<CatalogProduct>();
		for (int i = 0;i<PRODUCT_NUMBER;i++){
		persistentProducts.add(
				productService.create(CatalogProductBuilder.buildWithGeneratedAttributes(i+76152)));
		}
		return persistentProducts;
	}

	private void tearDown(Customer customer, Order order, List<CatalogProduct> products, Report... reports) {
		for (Report report:reports){
			orderService.deleteReport(report.getDocumentNumber());
		}
		orderService.deleteOrder(order.getOrderNumber());
		customerService.delete(customer.getCustomerNumber());
		for (CatalogProduct product:products)
			productService.delete(product.getProductNumber());
	}

	private void orderShouldBeConfirmable(Customer customer) {
		Page<ItemDto> risToBeConfirmed = reportItemService.retrieveAllToBeConfirmedByCustomer(customer, createPageRequest());
		assertThat(risToBeConfirmed, is(not(nullValue())));
		assertThat(risToBeConfirmed.getContent().isEmpty(), is(false));
	}

	private void orderShouldBePersisted(Order order) {
		assertThat(order.getId(), is(notNullValue()));
		for (OrderItem item : order.getItems()){
			assertNotNull(item);
			assertNotNull(item.getId());
			assertNotNull(item.getProduct());
			assertNotNull(item.getOrder().getId());
		};
	}

	private List<ItemDto> givenItemDtos(int quantity, Product... products) {
		List<ItemDto> ris = new ArrayList<ItemDto>();
		for (Product product:products){
			ItemDto reportItem = new ItemDto();
			reportItem.setProduct(product.getProductNumber());
			reportItem.setQuantity(quantity);
			ris.add(reportItem);
		}
		return ris;
	}

	private List<ItemDto> extractItemDtos(Order order) {
		List<ItemDto> items = new ArrayList<ItemDto>();
		for (OrderItem item:order.getItems())
			items.add(item.toItemDto());
		return items;
	}
	
	private List<ItemDto> extractItemDtos(Report order) {
		List<ItemDto> items = new ArrayList<ItemDto>();
		for (ReportItem item:order.getItems()){
			ItemDto iDto = item.toItemDto();
			iDto.setQuantityLeft(item.getQuantity());
			items.add(iDto);
		}
		return items;
	}

	private void assertPersisted(ConfirmationReport confirmationReport, Customer customer) {
		assertThat(confirmationReport, notNullValue());
		assertThat(confirmationReport.getItems().size(), equalTo(2));
		assertThat(confirmationReport.getItems().iterator().next().getType(), equalTo(ReportItemType.CONFIRM));
		
		for (ReportItem reportItem : confirmationReport.getItems()){
			assertThat(new ConfirmedSpecification(false, false).isSatisfiedBy(reportItem.getOrderItem()), is(true));
			assertThat(confirmationReport.getId(), notNullValue());
			assertThat(reportItem.getOrderItem().getId(), notNullValue());
			assertThat(reportItem.getOrderItem().getOrder().getId(), notNullValue());
			
			Set<ReportItem> confirmationReportItem = reportItem.getOrderItem().getAllHesOfType(ReportItemType.CONFIRM);
			assertThat(confirmationReportItem.size(), equalTo(1));
			
			ConfirmationReport crByReportItem = confirmationReportItem.iterator().next().getConfirmationReport();
			assertThat(crByReportItem.getDocumentNumber(), notNullValue());
			assertThat(crByReportItem.getInvoiceAddress(), notNullValue());
			assertThat(crByReportItem.getShippingAddress(), notNullValue());
		}
		
	}

	private void assertShippable(Customer customer) {
		Page<ItemDto> risToBeShipped = reportItemService.retrieveAllToBeShipped(customer, createPageRequest());
		assertTrue(risToBeShipped != null);
		assertThat(risToBeShipped.getTotalElements(), is(2L));
		assertThat(risToBeShipped.getContent().size(), equalTo(2));
	}

	private void assertPersisted(DeliveryNotes deliveryNotes, Customer customer) {
		assertThat(deliveryNotes.getId(), is(notNullValue()));
		assertThat(deliveryNotes.getItems().isEmpty(), is(false));
		
		for (ReportItem reportItem : deliveryNotes.getItems()){
			assertThat(reportItem.getType(), equalTo(ReportItemType.SHIP));
			assertThat(reportItem.getId(), is(notNullValue()));
			assertThat(reportItem.getDeliveryNotes().getId(), is(notNullValue()));
			assertThat(reportItem.getQuantity(), is(greaterThan(0)));
		}
	}

	private void assertInvoicable(Customer customer) {
		Page<ItemDto> risToBeInvoiced = reportItemService.retrieveAllToBeInvoiced(customer, createPageRequest());
		assertThat(risToBeInvoiced, is(notNullValue()));
		assertThat(risToBeInvoiced.getTotalElements(), is(greaterThan(0l)));
	}
	
	private List<ItemDto> itemDtosWithQty(int quantityToBeSet, List<ItemDto> ris) {
		for (ItemDto ri : ris){
			ri.setQuantityLeft(quantityToBeSet);
		}
		return ris;
	}
	
	private void assertShippable() {
		Page<ItemDto> risToBeShipped =  reportItemService.retrieveAllToBeShipped(createPageRequest());
		assertTrue("should still find items to be shipped", risToBeShipped.getTotalElements() != 0l);
		for (ItemDto ri : risToBeShipped)
			assertEquals("shipped quantity of item is false", new Integer(10), ri.getQuantity());
	}

	private void assertInvoicableQtyOfItemsIs(int invoiceableQuantity) {
		Page<ItemDto> risToBeInvoiced =  reportItemService.retrieveAllToBeInvoiced(createPageRequest());
		assertThat(risToBeInvoiced.getTotalElements(), is(greaterThan(0L)));
		for (ItemDto ri : risToBeInvoiced)
			assertThat(ri.getQuantity(), is(equalTo(invoiceableQuantity)));
	}
	
	private void assertShippableQtyOfItemsIs(int shippableQty) {
		Page<ItemDto> risToBeShipped =  reportItemService.retrieveAllToBeShipped(createPageRequest());
		assertThat(risToBeShipped.getTotalElements(), is(greaterThan(0L)));
		for (ItemDto ri : risToBeShipped)
			assertThat(ri.getQuantityLeft(), is(equalTo(shippableQty)));
	}

	private PageRequest createPageRequest() {
		return new PageRequest(0, 100);
	}

	private void assertCompleted() {
		List<ItemDto> risCompleted = reportItemService.retrieveAllCompleted(createPageRequest()).getContent();
		assertThat(risCompleted.isEmpty(), is(false));
	}

	private void assertPersisted(Receipt receipt) {
		assertTrue(receipt.getId() != null);
		assertThat(receipt.getCustomerNumber(), notNullValue());
		assertFalse(receipt.getItems().isEmpty());
		for (ReportItem he : receipt.getItems()){
			assertTrue(he.getType() == ReportItemType.PAID);
			assertTrue(he.getId() != null);
			assertTrue(he.getReceipt() != null);
			assertTrue(he.getReceipt().getId() != null);
			assertTrue(he.getQuantity() > 0);
		}
	}

	private void assertInvoiceAndItemsPersisted(Invoice invoice) {
		assertTrue(invoice.getId() != null);
		assertTrue(invoice.getDocumentNumber() != null);
		
		for (ReportItem he : invoice.getItems()){
			assertTrue(he.getId() != null);
			assertTrue(he.getType() == ReportItemType.INVOICE);
			assertTrue(he.getQuantity() > 0);
		}
	}
}
