package de.switajski.priebes.flexibleorders.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class ConfirmationItemToBeAgreedSpec implements Specification<ReportItem> {

    private CriteriaQuery<?> query;

    @Override
    public Predicate toPredicate(Root<ReportItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        this.query = query;
        
        Subquery<ConfirmationItem> subquery = createCiSubquery();
        return cb.in(root).value(subquery);
    }

    private Subquery<ConfirmationItem> createCiSubquery() {
        Subquery<ConfirmationItem> subquery = query.subquery(ConfirmationItem.class);
        Root<ConfirmationItem> ciRoot = subquery.from(ConfirmationItem.class);
        subquery.select(ciRoot);
        return subquery;
    }

}
