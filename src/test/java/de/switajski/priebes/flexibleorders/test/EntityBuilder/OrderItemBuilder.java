package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import java.util.Date;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.parameter.OrderParameter;

public class OrderItemBuilder extends ItemBuilder<OrderItem>{

	//copied from orderItem
	private Date expectedDelivery;
	private Integer quantityLeft;
	private int orderItemNumber;

	/**
	 * constructor with mandatory attributes
	 * 
	 * @param orderParameter
	 */
	public OrderItemBuilder(OrderParameter orderParameter){
		super(orderParameter.getCustomer(),
				orderParameter.getProduct(), 
				orderParameter.getOrderNumber(), 
				orderParameter.getProduct().getProductNumber(), 
				orderParameter.getProduct().getName(),
				orderParameter.getProduct().getPriceNet());
		expectedDelivery = orderParameter.getExpectedDelivery();

	}

	public OrderItem build(){
		OrderItem oi = new OrderItem(
				new OrderParameter(product, customer, quantity, orderNumber, new Date()));
		setSuperAttributes(oi);
		oi.setExpectedDelivery(expectedDelivery);
		oi.setOrderItemNumber(orderItemNumber);
		oi.setQuantityLeft(quantityLeft);
		return oi;
	}
	
	public static OrderItem buildWithGeneratedAttributes(Integer i){
		OrderItem orderItem = new OrderItem(
				new OrderParameter(
						ProductBuilder.buildWithGeneratedAttributes(i),
						CustomerBuilder.buildWithGeneratedAttributes(i),
						i,
						new Long(i),
						new Date()
						)
				);
		return orderItem;
	}

	public OrderItemBuilder setExpectedDelivery(Date expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
		return this;
	}

	public OrderItemBuilder setOrderItemNumber(int orderItemNumber) {
		this.orderItemNumber = orderItemNumber;
		return this;
	}

	public OrderItemBuilder setQuantityLeft(Integer quantityLeft) {
		this.quantityLeft = quantityLeft;
		return this;
	}

}
