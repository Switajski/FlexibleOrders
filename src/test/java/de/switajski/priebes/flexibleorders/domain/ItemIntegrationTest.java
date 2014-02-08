package de.switajski.priebes.flexibleorders.domain;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.ItemBuilder;
import de.switajski.priebes.flexibleorders.testhelper.AbstractIntegrationTest;

public class ItemIntegrationTest extends AbstractIntegrationTest<OrderItem> {
		
	@Autowired
	private OrderItemRepository itemRepo;
	
	@Autowired ReportRepository reportRepo;
	
	@Override
	protected OrderItem createEntity() {
		return ItemBuilder.buildWithGeneratedAttributes(13425);
	}

	@Override
	protected JpaRepository<OrderItem, Long> getRepository() {
		return (JpaRepository<OrderItem, Long>) itemRepo;
	}
	
	@Test
	public void shouldCascadePersistToReport(){
		String documentNumber = "235";
		OrderItem item = ItemBuilder.buildWithGeneratedAttributes(12);
		HandlingEvent he = new HandlingEvent(new DeliveryNotes(documentNumber, 
				null, AddressBuilder.buildWithGeneratedAttributes(9876)), 
				HandlingEventType.CONFIRM, item, 12, new Date());
		
		item.addHandlingEvent(he);
		itemRepo.save(item);
		
		Report report = reportRepo.findByDocumentNumber(documentNumber);
		assertNotNull(report);
		assertNotNull(report.getDocumentNumber());
	}
	
	@Test
	public void shouldRefreshHandlingEvent(){
		Report report = new DeliveryNotes("235", null, 
				AddressBuilder.buildWithGeneratedAttributes(1234));
		OrderItem item = ItemBuilder.buildWithGeneratedAttributes(12);
		HandlingEvent he = new HandlingEvent(report, 
				HandlingEventType.CONFIRM, item, 12, new Date());
		
		item.addHandlingEvent(he);
		itemRepo.save(item);
		
		item.addHandlingEvent(new HandlingEvent(new DeliveryNotes("12355", null, 
				AddressBuilder.buildWithGeneratedAttributes(654)), 
				HandlingEventType.CONFIRM, item, 20, null));
		itemRepo.save(item);
		
		item.addHandlingEvent(new HandlingEvent(report, 
				HandlingEventType.CONFIRM, item, 20, null));
		
		for (HandlingEvent he2:item.getDeliveryHistory()){
			assertNotNull(he2);
			assertNotNull(he2.getOrderItem());
			assertNotNull(he2.getReport());
		}
		
	}
	
	
}
