package de.switajski.priebes.flexibleorders.domain;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.parameter.OrderParameter;
import de.switajski.priebes.flexibleorders.integrationtest.AbstractIntegrationTest;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import de.switajski.priebes.flexibleorders.repository.ProductRepository;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CustomerBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.OrderItemBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.ProductBuilder;

public class OrderItemIntegrationTest extends AbstractIntegrationTest<OrderItem> {
		
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ShippingItemRepository shippingItemRepository;
	
	@Autowired
	CustomerRepository customerRepository;

	@Transactional
	@Test
	public void shouldOrderByDate(){
		List<Long> oiNrs = this.orderItemRepository.getAllOrderNumbers();
		Date date = null;
		long orderNumberTemp = 0;
		for (Long oiNr:oiNrs){
			List<OrderItem> ois = orderItemRepository.findByOrderNumber(oiNr);
			if (date==null){
				date = ois.get(0).getCreated();
				orderNumberTemp = ois.get(0).getOrderItemNumber();
			}
			else{
				assertTrue("OrderNumber not ordered by date:" + oiNr + ","+ orderNumberTemp ,
						ois.get(0).getCreated().compareTo(date)<=0
						);
				orderNumberTemp = ois.get(0).getOrderNumber();
				date = ois.get(0).getCreated();
			}
		}
	}

	@Override
	protected OrderItem createEntity() {
		Customer customer = new CustomerBuilder(
				"street", "city", 1234, "email@nowhere.com").build();
		customerRepository.save(customer);
		Product product = 
				new ProductBuilder(new Category(), 12345L, ProductType.PRODUCT)
		.build();

		productRepository.save(product);

		return new OrderItemBuilder(
				new OrderParameter(
						product, customer, 5, 13463L, new Date()
				)).build();
	}

	@Override
	protected JpaRepository<OrderItem, ?> getRepository() {
		return orderItemRepository;
	}
	
	
	
	
}
