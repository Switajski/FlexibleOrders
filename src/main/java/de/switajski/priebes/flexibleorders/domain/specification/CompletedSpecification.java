package de.switajski.priebes.flexibleorders.domain.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.switajski.priebes.flexibleorders.domain.Item;

public class CompletedSpecification extends ItemSpecification{

	@Override
	public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	boolean isSatisfiedBy(Item item) {
		return (new ShippedSpecification().isSatisfiedBy(item) && new PayedSpecification().isSatisfiedBy(item));
	}

}
