package de.switajski.priebes.flexibleorders.report;

import java.util.List;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class Order extends Report<OrderItem>{

	public Order(List<OrderItem> orderItems) {
		super(orderItems);
	}
	
}
