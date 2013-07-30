package de.switajski.priebes.flexibleorders.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.switajski.priebes.flexibleorders.json.JsonFilter;
import de.switajski.priebes.flexibleorders.report.Order;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController extends JsonController<Order> {
	
	@Autowired OrderItemService orderItemService;

	@Autowired
	public OrderController(OrderService crudServiceAdapter) {
		super(crudServiceAdapter);
	}

	@Override
	protected void resolveDependencies(Order entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Page<Order> findByFilterable(PageRequest pageRequest,
			JsonFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
