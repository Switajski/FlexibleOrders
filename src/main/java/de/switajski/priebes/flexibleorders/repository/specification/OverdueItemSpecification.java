package de.switajski.priebes.flexibleorders.repository.specification;

import java.util.function.Predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class OverdueItemSpecification implements Predicate<ReportItem>, Specification<ReportItem> {

    private static final String QTY = "quantity";

    @Override
    public boolean test(ReportItem ri) {
        return 0 < ri.overdue();
    }

    /**
     * <pre>
      * select ri.id, ri.dtype, ri.quantity, oi.name  from report_item ri
    join order_item oi on ri.order_item = oi.id
    where ri.quantity > 
    (
     select coalesce(sum(si.quantity),0) as successed 
     from report_item si
     where si.predecessor = ri.id
    )
     * </pre>
     */
    @Override
    public javax.persistence.criteria.Predicate toPredicate(
            Root<ReportItem> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb) {

        // Subquery<Integer> subquery = query.subquery(Integer.class);
        // Root<ReportItem> sumSuccessorsQuery =
        // subquery.from(ReportItem.class);
        // Expression<Integer> select = cb.<Integer>
        // sum(sumSuccessorsQuery.<Integer> get(QTY));
        // Expression<Integer> coalescted = cb.coalesce(select, new Integer(0));
        // subquery.select(coalescted);
        // subquery.where(cb.equal(sumSuccessorsQuery.get("predecessor"),
        // root.get("id")));

        return cb.greaterThan(root.get(QTY), root.get("overdue"));

    }

}
