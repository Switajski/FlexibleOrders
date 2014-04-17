package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.OriginSystem;

public class OrderBuilder {

	private String orderNumber;
	private Customer customer;
	private String customerEmail;
	private OriginSystem originSystem;
	private Set<OrderItem> items = new HashSet<OrderItem>();
	
	public Order build() {
		Order order = new Order();
		order.setOrderNumber(orderNumber);
		order.setCustomer(customer);
		order.setCustomerEmail(customerEmail);
		order.setOriginSystem(originSystem);
		order.setItems(items);
		return order;
	}

	public OrderBuilder setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
		return this;
	}

	public OrderBuilder setCustomer(Customer customer) {
		this.customer = customer;
		return this;
	}

	public OrderBuilder setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
		return this;
	}

	public OrderBuilder setOriginSystem(OriginSystem originSystem) {
		this.originSystem = originSystem;
		return this;
	}

	public OrderBuilder setItems(Set<OrderItem> items) {
		this.items = items;
		return this;
	}

	public OrderBuilder addOrderItem(OrderItem orderItem){
		this.items.add(orderItem);
		return this;
	}
	
}
