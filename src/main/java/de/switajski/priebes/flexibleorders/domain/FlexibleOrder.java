package de.switajski.priebes.flexibleorders.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class FlexibleOrder extends GenericEntity {

	/** 
	 * natural id
	 */
	@NotNull
	@Column(unique=true)
	private String orderNumber;
	
	@ManyToOne
	private Customer customer;
	
	/**
	 * in case of unregistered customers only a email address is available
	 */
	private String customerEmail;
	
	private OriginSystem originSystem;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	private Set<OrderItem> items = new HashSet<OrderItem>();
	
	@NotNull
	private Double vatRate = 0.19d;
	
	public FlexibleOrder() {}
	
	public FlexibleOrder(Customer customer, OriginSystem originSystem, String orderNumber){
		this.customer = customer;
		this.customerEmail = customer.getEmail();
		this.originSystem = originSystem;
		this.orderNumber = orderNumber;
	}
	
	/**
	 * in case of unregistered Customers
	 * @param customerEmail
	 * @param originSystem
	 * @param orderNumber
	 */
	public FlexibleOrder(String customerEmail, OriginSystem originSystem, String orderNumber){
		this.customerEmail = customerEmail;
		this.originSystem = originSystem;
		this.orderNumber = orderNumber;
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public OriginSystem getOriginSystem() {
		return originSystem;
	}

	public void setOriginSystem(OriginSystem originSystem) {
		this.originSystem = originSystem;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Set<OrderItem> getItems() {
		return items;
	}

	public void setItems(Set<OrderItem> items) {
		this.items = items;
	}
	
	public void addOrderItem(OrderItem item1) {
		if (item1.getOrder() == null)
			item1.setOrder(item1.getOrder());
		else if (!item1.getOrder().equals(this))
			throw new IllegalArgumentException("Item has already other order set");
		this.items.add(item1);
	}

	public Double getVatRate() {
		return this.vatRate;
	}
	
	@Override
	public String toString() {
		String itemsString = "";
		for (OrderItem oi:this.items)
			itemsString.concat(oi.toString());
		String s = "#"+getId().toString() + " Cust.:" + getCustomer().getId()+" "
				+ "(items: "+itemsString +")";
		return s;
	}

}
