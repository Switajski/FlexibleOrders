package de.switajski.priebes.flexibleorders.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.testhelper.AbstractTestWithSpringContext;

public class QueriesTest extends AbstractTestWithSpringContext{

	@PersistenceContext
	private EntityManager em;
	
	@Test
	public void shouldFindOrderItemsToBeConfirmed(){
		Query query = em.createQuery("SELECT oi from OrderItem oi where oi.deliveryHistory is empty");
		query.getResultList();
	}
	
	@Test
	public void shouldFindOrdersToBeConfirmed(){
		String qlString = "SELECT distinct(o) from FlexibleOrder o join o.items oi where oi.deliveryHistory is empty";
		Query query = em.createQuery(qlString);
		query.getResultList();
	}
	
	@Test
	public void shouldFindOrdersToBeShipped(){
		String qlString = "SELECT distinct(o) from FlexibleOrder o join o.items oi join oi.deliveryHistory dh where dh.type = "
				+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.CONFIRM";
		Query query = em.createQuery(qlString);
		query.getResultList();
	}
	
	@Test
	public void shouldFindOrdersToBePaid(){
		String qlString = "SELECT distinct(o) from FlexibleOrder o join o.items oi join oi.deliveryHistory dh where dh.type = "
				+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.SHIP and "
				+ "dh.type != de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID";
		Query query = em.createQuery(qlString);
		query.getResultList();
	}
	
	@Test
	public void shouldFindOrdersCompleted(){
		String qlString = "SELECT distinct(o) from FlexibleOrder o join o.items oi join oi.deliveryHistory dh where dh.type = "
				+ "de.switajski.priebes.flexibleorders.domain.HandlingEventType.PAID";
		Query query = em.createQuery(qlString);
		query.getResultList();
	}

}
