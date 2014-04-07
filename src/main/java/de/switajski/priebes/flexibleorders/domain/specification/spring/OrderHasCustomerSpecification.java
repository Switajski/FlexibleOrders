package de.switajski.priebes.flexibleorders.domain.specification.spring;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Order;

public class OrderHasCustomerSpecification implements Specification<Order> {

	private Customer customer;

	public OrderHasCustomerSpecification(Customer customer) {
		this.customer = customer;
	}
	
	@Override
	public Predicate toPredicate(Root<Order> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		return cb.equal(root.get(Customer.class.getSimpleName().toLowerCase()), customer);
	}

	
}
