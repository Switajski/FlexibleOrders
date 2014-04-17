package de.switajski.priebes.flexibleorders.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class HasConfirmHandlingEvents implements Specification<OrderItem> {

	@Override
	public Predicate toPredicate(Root<OrderItem> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		SetJoin<OrderItem, ReportItem> heJoin = root.joinSet("deliveryHistory");
		Predicate hasConfirmHe = 
				cb.equal(heJoin.<ReportItemType>get("type"), cb.literal(ReportItemType.CONFIRM));
		return hasConfirmHe;
	}

}
