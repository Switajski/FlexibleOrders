package de.switajski.priebes.flexibleorders.domain.parameter;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.util.Assert;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Product;

public class OrderParameter {

	private Product product;
	
	private Customer customer;
	
	private Integer quantity;
	
	private Long orderNumber;
	
	private Date expectedDelivery;
	
	/**
	 * 
	 * @param product must not be null and have a price
	 * @param customer must not be null
	 * @param quantity must not be null
	 * @param orderNumber must not be null
	 * @param expectedDelivery
	 */
	public OrderParameter(
			@NotNull Product product,
			@NotNull Customer customer,
			@NotNull Integer quantity,
			@NotNull Long orderNumber,
			Date expectedDelivery) {
		Assert.notNull(product.getPriceNet());;
		
		this.product = product;
		this.customer = customer;
		this.quantity = quantity;
		this.orderNumber = orderNumber;
		this.expectedDelivery = expectedDelivery;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Date getExpectedDelivery() {
		return expectedDelivery;
	}

	public void setExpectedDelivery(Date expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
	}
}
