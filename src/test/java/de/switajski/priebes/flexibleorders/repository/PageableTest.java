package de.switajski.priebes.flexibleorders.repository;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.testhelper.AbstractTestWithProductiveSpringContext;

/**
 * Exploratory test to figure out the Pageable functionality of Spring Data's repository
 * @author Marek Switajski
 *
 */
public class PageableTest extends AbstractTestWithProductiveSpringContext{

	@Autowired private OrderItemRepository orderItemRepo;
	
	@Ignore
	@Test
	public void shouldReturnPage(){
		int page = 2;
		int size = 5;
		PageRequest pageRequest = new PageRequest(page, size);
		
		Page<OrderItem> pageList = orderItemRepo.findAll(pageRequest);
		
		assertFalse(pageList.getContent().isEmpty());		
	}
}
