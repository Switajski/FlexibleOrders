package de.switajski.priebes.flexibleorders.repository.specification;

import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class OrderSpecifications {

	public static Specification<Order> hasCustomer(final Customer customer){
		return new Specification<Order>() {
			@Override
			public Predicate toPredicate(Root<Order> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(Customer.class.getSimpleName().toLowerCase()), customer);
			}
		};
	}
	
	public static Specification<Order> hasEmptyDeliveryHistory(){
		return new Specification<Order>() {
			@Override
			public Predicate toPredicate(Root<Order> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<Set<ReportItem>> itemsJoin = root.<Set<OrderItem>>get("items").<Set<ReportItem>>get("deliveryHistory");
				Predicate emptyDeliveryHistoryPredicate = 
						cb.isEmpty(itemsJoin);
				return emptyDeliveryHistoryPredicate;
			}
		};
	}
}
