package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.switajski.priebes.flexibleorders.domain.specification.OrderedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.OriginSystem;

@Entity
@JsonAutoDetect
public class Item extends GenericEntity implements Comparable<Item> {

	@ManyToOne
	private Customer customer;
	
	@OneToOne
	private DeliveryHistory deliveryHistory;
	
	private OriginSystem originSystem;

	@Embedded
	private OrderedSpecification orderedSpecification;
	
	public Item() {}
	
	@Override
	public int compareTo(Item o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public OrderedSpecification getOrderedSpecification() {
		return orderedSpecification;
	}

	public void setOrderedSpecification(OrderedSpecification orderSpecification) {
		this.orderedSpecification = orderSpecification;
	}

	public DeliveryHistory getDeliveryHistory() {
		return deliveryHistory;
	}

	public void setDeliveryHistory(DeliveryHistory deliveryHistory) {
		this.deliveryHistory = deliveryHistory;
	}

	public Long getProductNumber(){
		return this.getOrderedSpecification().getProductNumber();
	}
	
	public String getProductName(){
		return this.getOrderedSpecification().getProductName();
	}
	
	public int getQuantity(){
		return this.getOrderedSpecification().getQuantity();
	}
	
	public Long getOrderNumber(){
		return this.getOrderedSpecification().getOrderNumber();
	}
	
	public String getCustomerEmail(){
		return this.getOrderedSpecification().getCustomerEmail();
	}

	public OriginSystem getOriginSystem() {
		return originSystem;
	}

	public void setOriginSystem(OriginSystem originSystem) {
		this.originSystem = originSystem;
	}
	
	public void setProduct(CatalogProduct productFromCatalog){
		getOrderedSpecification().setProductName(productFromCatalog.getName());
		getOrderedSpecification().setProductNumber(productFromCatalog.getProductNumber());
	}
	
}
