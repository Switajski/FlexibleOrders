package de.switajski.priebes.flexibleorders.repository.specification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.Order;

public class OrderCreatedBetweenSpecification implements Specification<Order> {
    
    private Date from, to;
    
    public OrderCreatedBetweenSpecification(LocalDate from, LocalDate to) {
        this.from = convert(from);
        this.to = convert(to);
    }
    
    public static OrderCreatedBetweenSpecification inMonth(LocalDate date){
        LocalDate from = date.dayOfMonth().withMinimumValue();
        LocalDate to = date.dayOfMonth().withMaximumValue();
        return new OrderCreatedBetweenSpecification(from, to);
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.between(root.<Date>get("created"), from, to);
    }

    private  Date convert(LocalDate date){
        return date.toDateTimeAtStartOfDay().toDate();
    }
}
