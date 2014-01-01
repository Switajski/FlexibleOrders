package de.switajski.priebes.flexibleorders.domain.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.Item;

public class PayedSpecification extends ItemSpecification {

	public long accountNumber;
	
	public long getAccountNumber() {
		return accountNumber;
	}
	
	public PayedSpecification() {}
	
	public PayedSpecification(long accountNumber){
		this.accountNumber = accountNumber;
	}

	@Override
	public Predicate toPredicate(Root<Item> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSatisfiedBy(Item item) {
		if (item.getDeliveryHistory().getAllHesOfType(HandlingEventType.PAY).isEmpty())
			return false;
		else return true;
	}
}
