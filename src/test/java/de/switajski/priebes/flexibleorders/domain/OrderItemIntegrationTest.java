package de.switajski.priebes.flexibleorders.domain;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.ShippingItemRepository;

@RooIntegrationTest(entity = OrderItem.class)
public class OrderItemIntegrationTest {
		
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
//				System.out.println(ois.get(0).getCreated());
				orderNumberTemp = ois.get(0).getOrderNumber();
				date = ois.get(0).getCreated();
			}
		}
	}
	
	
}
