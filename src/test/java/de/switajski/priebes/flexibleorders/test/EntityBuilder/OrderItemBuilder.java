package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.util.Date;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;

public class OrderItemBuilder implements Builder<OrderItem>{

	// not null(s)
	private Date created = new Date();
	private Long orderNumber = 1l;
	private int quantity = 1;
	private int quantityLeft = 1;
	
	// dependencies
	private Customer customer;
	private Product product;
	
	public OrderItemBuilder(){	}
	
	public OrderItemBuilder withQuantityLeft(int quantityLeft){
		this.quantityLeft = quantityLeft;
		return this;
	}
	
	public OrderItemBuilder withQuantity(int quantity){
		this.quantity = quantity;
		return this;
	}
	
	public OrderItemBuilder withCustomer(Customer customer){
		this.customer = customer;
		return this;
	}
	
	public OrderItemBuilder withProduct(Product product){
		this.product = product;
		return this;
	}
	
	public OrderItem build(){
		if (customer == null)
			customer = new CustomerBuilder().build();
		if (product == null)
			product = new ProductBuilder().build();
		
		OrderItem oi = new OrderItem();
		oi.setCreated(created);
		oi.setOrderNumber(orderNumber);
		oi.setQuantity(quantity);
		oi.setQuantityLeft(quantityLeft);
		oi.setCustomer(customer);
		oi.setProduct(product);
		return oi;
	}
}
