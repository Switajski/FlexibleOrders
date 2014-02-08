package de.switajski.priebes.flexibleorders.domain.specification.spring;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class HasCustomerSpecification implements Specification<OrderItem> {

	private Customer customer;

	public HasCustomerSpecification(Customer customer) {
		this.customer = customer;
	}
	
	@Override
	public Predicate toPredicate(Root<OrderItem> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		Predicate whereCustomer = cb.equal(root.get(Customer.class.getSimpleName().toLowerCase()),
				customer);
		return whereCustomer;
	}
	

}
