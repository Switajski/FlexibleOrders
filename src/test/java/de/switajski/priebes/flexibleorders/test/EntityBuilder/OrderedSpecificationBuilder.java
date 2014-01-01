package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.specification.OrderedSpecification;

public class OrderedSpecificationBuilder implements Builder<OrderedSpecification> {

	private Integer quantity;
	private Long orderNumber;
	private Long productNumber;
	private String productName;
	private String customerEmail;
	
	@Override
	public OrderedSpecification build() {
		OrderedSpecification o = new OrderedSpecification();
		o.setQuantity(quantity);
		o.setOrderNumber(orderNumber);
		o.setProductName(productName);
		o.setProductNumber(productNumber);
		o.setCustomerEmail(customerEmail);
		return o;
	}
	
	public static OrderedSpecification buildWithGeneratedAttributes(Integer i){
		return new OrderedSpecificationBuilder().generateAttributes(i).build();
	}

	public OrderedSpecificationBuilder generateAttributes(Integer i){
		return new OrderedSpecificationBuilder()
		.setCustomerEmail("customerEmail"+i)
		.setOrderNumber(i.longValue())
		.setProductName("productName"+i)
		.setProductNumber(i.longValue())
		.setQuantity(i);
	}
	
	public OrderedSpecificationBuilder setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	public OrderedSpecificationBuilder setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
		return this;
	}

	public OrderedSpecificationBuilder setProductNumber(Long productNumber) {
		this.productNumber = productNumber;
		return this;
	}

	public OrderedSpecificationBuilder setProductName(String productName) {
		this.productName = productName;
		return this;
	}

	public OrderedSpecificationBuilder setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
		return this;
	}

}
