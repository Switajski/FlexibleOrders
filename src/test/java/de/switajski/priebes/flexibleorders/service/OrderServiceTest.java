package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
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
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CustomerBuilder;


/**
 * Test with database to proof core order process functionality.
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
	@Autowired OrderServiceImpl transitionService;
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
		
		String orderConfirmationNo = "345";
		item = createOrderConfirmation(item, QUANTITY_IN_NEXT_STATE, orderConfirmationNo);
		assertFalse(new ConfirmedSpecification(false, false).isSatisfiedBy(item));
		assertConfirmAttributes(item, 1);
		
		transitionService.confirm(item, QUANTITY_IN_NEXT_STATE, orderConfirmationNo, AMOUNT);
		assertTrue(new ConfirmedSpecification(false, false).isSatisfiedBy(item));
		assertConfirmAttributes(item, 2);		
	}
	
	private void assertConfirmAttributes(OrderItem item, int numberOfItems) {
		Set<HandlingEvent> confirmedEvents = item.getAllHesOfType(HandlingEventType.CONFIRM);
		assertEquals(numberOfItems, confirmedEvents.size());
		ConfirmationReport orderConfirmation = confirmedEvents.iterator().next().getOrderConfirmation();

		assertNotNull(orderConfirmation.getDocumentNumber());
		assertNotNull(orderConfirmation.getInvoiceAddress());
		assertNotNull(orderConfirmation.getShippingAddress());
//		TODO implement vatRate in Report / make a dependency to vatRate
//		assertNotNull(orderConfirmation.getVatRate());
	}

	private OrderItem createOrderConfirmation(OrderItem item, int quantity, String orderConfirmationNo) {
		ConfirmationReport param = new ConfirmationReport(
				orderConfirmationNo, INVOICE_ADDRESS, INVOICE_ADDRESS, new ConfirmedSpecification(false, false));
		item = transitionService.confirm(
				item, quantity, AMOUNT, param, new ConfirmedSpecification(false, false));
		return item;
	}
	
	private OrderItem createItemAndOrder(int quantity, Integer random){
		Customer customer = customerService.create(
				CustomerBuilder.buildWithGeneratedAttributes(random));
		CatalogProduct product = productService.create(
				CatalogProductBuilder.buildWithGeneratedAttributes(random));

		return transitionService.order(customer, random.toString(), 
				product.toProduct(), quantity, new Amount(BigDecimal.TEN, Currency.EUR));
	}
	

	@Test
	public void shouldDeconfirm(){
		OrderItem item = createItemAndOrder(10, 11);
		item = createOrderConfirmation(item, 10, "9087");
		
		assertTrue(new ConfirmedSpecification(false, false).isSatisfiedBy(item));
		item = transitionService.deconfirm(item);
		
		assertFalse(new ConfirmedSpecification(false, false).isSatisfiedBy(item));
	}


	@Test
	public void shouldwithdrawInvoiceItemAndShipment(){
		OrderItem item = createItemAndOrder(10, 17654);
		item = createOrderConfirmation(item, 5, "Oasdf");
		
		String invoiceNo = "invoiceno";
		item = transitionService.shipAndInvoice(item, 5, invoiceNo, null, 
				AddressBuilder.buildWithGeneratedAttributes(12));
		assertTrue(new ShippedSpecification(false, false).isSatisfiedBy(item));
		
		item = transitionService.withdrawInvoiceItemAndShipment(item);
		assertFalse(new ShippedSpecification(false, false).isSatisfiedBy(item));
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void shouldRejectOrderConfirmation(){
		OrderItem item = createItemAndOrder(10, 15);
		createOrderConfirmation(item, 51, "tempOrderConfirmationNumber");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldRejectInvoicing(){
		OrderItem item = createItemAndOrder(1411, 112345);
		createOrderConfirmation(item, 5, "tempOrderConfirmationNumber");
		
		transitionService.shipAndInvoice(item, 10, "invoiceNomnasdf", null, AddressBuilder.buildWithGeneratedAttributes(67766767));
	}
	
	@Test
	public void shouldReceivePayment(){
		OrderItem item = createItemAndOrder(10, 14);
		String orderConfirmationNo = "1235";
		item = createOrderConfirmation(item, 5, orderConfirmationNo);
		
		transitionService.receivePayment(orderConfirmationNo, new Date());
	}

}
