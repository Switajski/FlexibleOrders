package de.switajski.priebes.flexibleorders.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.reference.OriginSystem;

@Entity
@Table(name="c_order")
public class Order extends GenericEntity {

	@Transient
	public static final double VAT_RATE = 0.19d;
	
	/** 
	 * natural id
	 */
	@NotNull
	@Column(unique=true)
	private String orderNumber;
	
	@Embedded
	private PurchaseAgreement puchaseAgreement;
	
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
	
	public Order() {}
	
	public Order(Customer customer, OriginSystem originSystem, String orderNumber){
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
	public Order(String customerEmail, OriginSystem originSystem, String orderNumber){
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
	
	public boolean remove(OrderItem orderItem){
		return this.items.remove(orderItem);
	}
	
	public void addOrderItem(OrderItem item) {
		if (item.getOrder() == null)
			item.setOrder(this);
		else if (!item.getOrder().equals(this))
			throw new IllegalArgumentException("Item has already other order set");
		this.items.add(item);
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

	@JsonIgnore
	public PurchaseAgreement getPurchaseAgreement() {
		return puchaseAgreement;
	}

	public void setPurchaseAgreement(PurchaseAgreement purchaseAgreement) {
		this.puchaseAgreement = purchaseAgreement;
	}

}
