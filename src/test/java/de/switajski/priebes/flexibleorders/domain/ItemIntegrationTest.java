package de.switajski.priebes.flexibleorders.domain;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.integrationtest.AbstractIntegrationTest;
import de.switajski.priebes.flexibleorders.repository.CategoryRepository;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.ItemRepository;
import de.switajski.priebes.flexibleorders.repository.CatalogProductRepository;
import de.switajski.priebes.flexibleorders.service.ItemServiceImpl;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.ItemBuilder;

public class ItemIntegrationTest extends AbstractIntegrationTest<Item> {
		
	@Autowired
	private ItemRepository itemRepo;
	
	@Autowired
	private ItemServiceImpl itemService;
	
	@Autowired
	private CatalogProductRepository productRepository;
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired 
	private CategoryRepository categoryRepository;

//	@Transactional
//	@Test
//	public void shouldOrderByDate(){
//		List<Long> oiNrs = this.itemService.getAllOrderNumbers();
//		Date date = null;
//		long orderNumberTemp = 0;
//		for (Long oiNr:oiNrs){
//			List<Item> ois = itemRepo.findByOrderNumber(oiNr);
//			if (date==null){
//				date = ois.get(0).getCreated();
//				orderNumberTemp = ois.get(0).getOrderItemNumber();
//			}
//			else{
//				assertTrue("OrderNumber not ordered by date:" + oiNr + ","+ orderNumberTemp ,
//						ois.get(0).getCreated().compareTo(date)<=0
//						);
//				date = ois.get(0).getCreated();
//			}
//		}
//	}

	@Override
	protected Item createEntity() {
		return new ItemBuilder().build();
	}

	@Override
	protected JpaRepository<Item, Long> getRepository() {
		return (JpaRepository<Item, Long>) itemRepo;
	}
	
	
	
	
}
