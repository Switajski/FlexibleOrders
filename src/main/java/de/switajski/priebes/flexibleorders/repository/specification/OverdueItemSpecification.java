package de.switajski.priebes.flexibleorders.repository.specification;

import java.util.function.Predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class OverdueItemSpecification implements Predicate<ReportItem>, Specification<ReportItem> {

    @Override
    public boolean test(ReportItem ri) {
        return 0 < ri.overdue();
    }

    @Override
    public javax.persistence.criteria.Predicate toPredicate(
            Root<ReportItem> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb) {
        return cb.greaterThan(root.get("overdue"), 0);
    }

}
