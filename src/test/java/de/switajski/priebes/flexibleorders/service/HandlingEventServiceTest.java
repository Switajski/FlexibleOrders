package de.switajski.priebes.flexibleorders.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.CatalogProduct;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmationSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.DelinquentInvoiceSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.DeliverySpecification;
import de.switajski.priebes.flexibleorders.domain.specification.OrderedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CatalogProductBuilder;

public class HandlingEventServiceTest {

	private HandlingEventService handlingEventService;
	private Customer customer;
	private CatalogProduct product;
	
	@Before
	public void setUp(){
		product = CatalogProductBuilder.buildWithGeneratedAttributes(123);
		customer = CustomerBuilder.buildWithGeneratedAttributes(43);
	}
	
	@Test
	public void shouldCreateOrderItem(){
		Item item = createOrderItem();
		
		assertNotNull(item.getOrderedSpecification());
		assertNotNull(item.getDeliveryHistory());
		
		assertTrue(new OrderedSpecification().isSatisfiedBy(item));
		assertFalse(new ConfirmedSpecification().isSatisfiedBy(item));
		assertFalse(new DelinquentInvoiceSpecification(new Date()).isSatisfiedBy(item, item.getCustomer()));
		assertFalse(new ConfirmedSpecification().isSatisfiedBy(item));
		assertFalse(new ShippedSpecification().isSatisfiedBy(item));
		assertFalse(new CompletedSpecification().isSatisfiedBy(item));
	}

	private Item createOrderItem() {
		Item item = new Item(product, customer, new DeliverySpecification(15, 12356L)); 
		Item Item = handlingEventService.createItemEvent(item);
		return Item;
	}
	
	@Test
	public void shouldConfirmOrderItem(){
		ConfirmationSpecification cp = new ConfirmationSpecification( 
				new Amount(new BigDecimal("23.23")));
		
		handlingEventService.addConfirmationEvent(orderItem, cp);
	}
}
