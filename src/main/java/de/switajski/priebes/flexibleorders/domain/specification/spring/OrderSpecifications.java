package de.switajski.priebes.flexibleorders.domain.specification.spring;

import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class OrderSpecifications {

	public static Specification<FlexibleOrder> hasCustomer(final Customer customer){
		return new Specification<FlexibleOrder>() {
			@Override
			public Predicate toPredicate(Root<FlexibleOrder> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(Customer.class.getSimpleName().toLowerCase()), customer);
			}
		};
	}
	
	public static Specification<FlexibleOrder> hasEmptyDeliveryHistory(){
		return new Specification<FlexibleOrder>() {
			@Override
			public Predicate toPredicate(Root<FlexibleOrder> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<Set<HandlingEvent>> itemsJoin = root.<Set<OrderItem>>get("items").<Set<HandlingEvent>>get("deliveryHistory");
				Predicate emptyDeliveryHistoryPredicate = 
						cb.isEmpty(itemsJoin);
				return emptyDeliveryHistoryPredicate;
			}
		};
	}
}
