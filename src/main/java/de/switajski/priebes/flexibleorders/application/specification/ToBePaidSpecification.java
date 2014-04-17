package de.switajski.priebes.flexibleorders.application.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class ToBePaidSpecification extends ItemSpecification {
	
	@Override
	public boolean isSatisfiedBy(OrderItem item) {
		return (new ShippedSpecification(false, false).isSatisfiedBy(item) && 
				!(new CompletedSpecification().isSatisfiedBy(item)));
	}
	
	@Override
	public Predicate toPredicate(Root<OrderItem> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		throw new NotImplementedException();
	}
}
