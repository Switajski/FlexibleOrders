package de.switajski.priebes.flexibleorders.domain.specification.spring;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class HasConfirmHandlingEvents implements Specification<OrderItem> {

	@Override
	public Predicate toPredicate(Root<OrderItem> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		SetJoin<OrderItem, HandlingEvent> heJoin = root.joinSet("deliveryHistory");
		Predicate hasConfirmHe = 
				cb.equal(heJoin.<HandlingEventType>get("type"), cb.literal(HandlingEventType.CONFIRM));
		return hasConfirmHe;
	}

}
