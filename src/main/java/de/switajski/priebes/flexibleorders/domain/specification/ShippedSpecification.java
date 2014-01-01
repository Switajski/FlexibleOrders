package de.switajski.priebes.flexibleorders.domain.specification;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.Item;

@Embeddable
public class ShippedSpecification extends ItemSpecification {

	private long invoiceNumber;
	
	private Date dueDate;
	
//	private Address address;
	
	public ShippedSpecification() {}
	
	public ShippedSpecification(long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public long getInvoiceNumber() {
		return invoiceNumber;
	}

	@Override
	public boolean isSatisfiedBy(Item item) {
		if (item.getDeliveryHistory().getAllHesOfType(HandlingEventType.SHIP).isEmpty()) return false;
		
		if (getHandledQuantityFromEvents(item, HandlingEventType.ORDERCONFIRM) <= 
				getHandledQuantityFromEvents(item, HandlingEventType.SHIP))
			return true;
		
		return false;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setInvoiceNumber(long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	@Override
	public Predicate toPredicate(Root<Item> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		// TODO Auto-generated method stub
		return null;
	}

//	public Address getAddress(){
//		return address;
//	}
}
