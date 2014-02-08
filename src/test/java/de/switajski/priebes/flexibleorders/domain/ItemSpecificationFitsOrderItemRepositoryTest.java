package de.switajski.priebes.flexibleorders.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ItemSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.OrderedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.service.PriebesJoomlaImporterService;
import de.switajski.priebes.flexibleorders.testhelper.AbstractTestWithSpringContext;

public class ItemSpecificationFitsOrderItemRepositoryTest extends AbstractTestWithSpringContext {

	boolean DEBUG = true;
	
	@Autowired
	private OrderItemRepository itemRepo;
	
	private static Logger log = Logger.getLogger(PriebesJoomlaImporterService.class);
	
	@Test
	public void shouldFindConfirmed(){
		ConfirmedSpecification confSpec = new ConfirmedSpecification(false, false);
		
		isSatisfiedAndToPredicateShouldHaveSameResult(confSpec);
	}
	
	@Test
	public void shouldFindOrdered(){
		OrderedSpecification orderedSpec = new OrderedSpecification();
		
		isSatisfiedAndToPredicateShouldHaveSameResult(orderedSpec);
	}
	
	@Test
	public void shouldFindShipped(){
		ShippedSpecification shippedSpec = new ShippedSpecification(false, false);
		
		isSatisfiedAndToPredicateShouldHaveSameResult(shippedSpec);
	}
	
	@Test
	public void shouldFindToBeShipped(){
		List<OrderItem> ois = itemRepo.findAllShipped();
		
		for (OrderItem oi:ois){
			assertTrue(new ConfirmedSpecification(false, false).isSatisfiedBy(oi));
			assertFalse(new ShippedSpecification(false, false).isSatisfiedBy(oi));
		}
	}
	
	@Test
	public void resultShouldHaveShipAndConfirmType(){
		List<OrderItem> ois = itemRepo.findAllShipped();
		
		for (OrderItem oi:ois){
			assertTrue(hasShipAndConfirmType(oi.getDeliveryHistory()));
		}
	}

	private boolean hasShipAndConfirmType(Set<HandlingEvent> deliveryHistory) {
		boolean hasShipType = false;
		boolean hasConfirmType = false;
		for (HandlingEvent he :deliveryHistory){
			if (he.getType().equals(HandlingEventType.SHIP))
				hasShipType = true;
			if (he.getType().equals(HandlingEventType.SHIP))
				hasConfirmType = true;
		}
		return (hasShipType && hasConfirmType);
	}

	private void isSatisfiedAndToPredicateShouldHaveSameResult(
			ItemSpecification itemSpec) {
		assertFalse(itemRepo.findAll().isEmpty());
		List<OrderItem> ois = itemRepo.findAll(itemSpec);
		assertFalse("Did not find any", ois.isEmpty());
		if (DEBUG) log.debug(itemSpec.getClass().getName()+" query contains "+ ois.size() +" out of "+itemRepo.count()+" orderItems");
		if (DEBUG) log.debug(ois.get(0));
		int i = 0;
		for (OrderItem oi:ois){
			if (DEBUG) log.debug(i++ +".check");
			assertTrue(itemSpec.isSatisfiedBy(oi));
		}
	}
}
