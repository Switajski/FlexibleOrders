package de.switajski.priebes.flexibleorders.domain.specification.spring;

import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class HasEmptyDeliveryHistorySpecification implements Specification<OrderItem>{

	@Override
	public Predicate toPredicate(Root<OrderItem> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		Path<Set<HandlingEvent>> deliveryHistory = root.get("deliveryHistory");
		Predicate emptyDeliveryHistoryPredicate = 
				cb.isEmpty(deliveryHistory);
		return emptyDeliveryHistoryPredicate;
	}

	
}
