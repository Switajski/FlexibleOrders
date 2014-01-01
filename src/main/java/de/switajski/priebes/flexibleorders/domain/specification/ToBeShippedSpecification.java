package de.switajski.priebes.flexibleorders.domain.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.switajski.priebes.flexibleorders.domain.Item;

public class ToBeShippedSpecification extends ItemSpecification {

	public ToBeShippedSpecification() {	}

	@Override
	public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSatisfiedBy(Item item) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
