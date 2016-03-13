package de.switajski.priebes.flexibleorders.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class HasCustomerSpecification implements Specification<ReportItem> {

    private Customer customer;

    private String customerNumber;

    public HasCustomerSpecification(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public HasCustomerSpecification(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Predicate toPredicate(
            Root<ReportItem> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb) {
        if (customer != null) return cb.equal(root.get("orderItem")
                .get("customerOrder")
                .get("customer"), customer);
        else return cb.equal(root.get("orderItem")
                .get("customerOrder")
                .get("customer")
                .get("customerNumber"), customerNumber);
    }

}
