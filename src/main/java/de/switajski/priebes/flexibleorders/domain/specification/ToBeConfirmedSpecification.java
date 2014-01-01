package de.switajski.priebes.flexibleorders.domain.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.Item;


public class ToBeConfirmedSpecification implements Specification<Item> {

	//TODO: check if there is way to combine two specifications with AND
	
	public boolean isSatisfiedBy(Item item) {
		if (new OrderedSpecification().isSatisfiedBy(item)
				&& !new ConfirmedSpecification().isSatisfiedBy(item))
			return true;
		return false;
	}

	@Override
	public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		// TODO Auto-generated method stub
		return null;
	}
}
