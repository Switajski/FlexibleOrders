package de.switajski.priebes.flexibleorders.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.ReportItem;

public class ConfirmationItemToBeShippedSpec implements Specification<ReportItem>{

	/**
	 * http://stackoverflow.com/questions/3997070/jpa-criteria-tutorial
	 */
	@Override
	public Predicate toPredicate(Root<ReportItem> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		Expression<Integer> qty = root.get("quantity").as(Integer.class);
		Predicate hasSum7Predicate = cb.equal(cb.sum(qty), new Integer(7));
		return hasSum7Predicate;
	}
	
}
