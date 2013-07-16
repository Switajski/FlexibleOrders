package de.switajski.priebes.flexibleorders.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.reference.Status;

public class Order {
	
	private Customer customer;

	private Long orderNumber;

	private HashMap<Status, List<OrderItem>> orderItems = new HashMap<Status, List<OrderItem>>();

	private Date created;
	
	private double taxRate = 0.19;
	
	public Order(List<OrderItem> orderItems) {
		setOrderItems(orderItems);
	}

	public Customer getCustomer() {
		return customer;
	}

	private void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	private void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<OrderItem> getOrderItems() {
		ArrayList<OrderItem> ois = new ArrayList<OrderItem>();
		for (Status s:Status.values())
			for (OrderItem oi:orderItems.get(s)){
				ois.add(oi);
			}
		return ois;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		if (orderItems==null) throw new IllegalArgumentException("Items for report must not be null");
		
		OrderItem orderItem = orderItems.get(0);
		setCustomer(orderItem.getCustomer());
		setOrderNumber(orderItem.getOrderNumber());
		
		setOrderItemsToHashMap(orderItems);
	}
	
	
	private void setOrderItemsToHashMap(List<OrderItem> orderItems2) {
		for (Status s:Status.values())
			this.orderItems.put(s, new ArrayList<OrderItem>());
		for (OrderItem oi:orderItems2){
			this.orderItems.get(oi.getStatus()).add(oi);
		}
		
	}

	public List<OrderItem> getOrderItems(Status status){
		return orderItems.get(status);
	}

	public Status getStatus() {
		int size = countOrderItems(); 
		for (Status status:Status.values())
			if (size == getOrderItems(status).size())
				return status;
		return null;
	}
	
	public int countOrderItems(){
		return getOrderItems().size();
	}

	public Date getCreated() {
		return created;
	}

	private void setCreated(Date created) {
		this.created = created;
	}

	public BigDecimal getNetAmount() {
		BigDecimal netAmount = new BigDecimal(0);
		for (OrderItem orderItem:getOrderItems()){
			BigDecimal amount = 
					orderItem.getPriceNet()
					.multiply(new BigDecimal(orderItem.getQuantity())); 
			netAmount.add(amount);
		}
		return netAmount;
	}

	public BigDecimal getGrossAmount() {
		BigDecimal grossAmount = getNetAmount();
		return grossAmount.multiply(new BigDecimal(getTaxRate()).add(BigDecimal.ONE));
	}

	public double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(double tax) {
		this.taxRate = tax;
	}

}
