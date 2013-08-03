package de.switajski.priebes.flexibleorders.report;

import java.util.List;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;

public class Order extends Report<OrderItem>{

	public Order(List<OrderItem> orderItems) {
		super(orderItems);
	}
	
}
