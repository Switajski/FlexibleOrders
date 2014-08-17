package de.switajski.priebes.flexibleorders.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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

import de.switajski.priebes.flexibleorders.domain.embeddable.AgreementDetails;
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
	private AgreementDetails agreementDetails;
	
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
	
	@JsonIgnore
	public List<OrderItem> getItemsOrdered() {
		List<OrderItem> ris = new ArrayList<OrderItem>(getItems());
		Collections.sort(ris, new Comparator<OrderItem>(){

			@Override
			public int compare(OrderItem r1, OrderItem r2) {
				Product p1 = r1.getProduct();
				Product p2 = r2.getProduct();
				if (p1.hasProductNo() && p2.hasProductNo())
					return p1.getProductNumber().compareTo(p2.getProductNumber());
				else if (!p1.hasProductNo() && !p2.hasProductNo()){
					return p1.getName().compareTo(p2.getName());
				} else if (p1.hasProductNo()){
					return 1;
				}
				else if (p2.hasProductNo()){
					return -1;
				}
				
				else return 0;
				
			}
			
		});
		return ris;
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
	public AgreementDetails getAgreementDetails() {
		return agreementDetails;
	}

	public void setAgreementDetails(AgreementDetails agreementDetails) {
		this.agreementDetails = agreementDetails;
	}

}
