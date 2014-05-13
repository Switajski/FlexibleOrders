package de.switajski.priebes.flexibleorders.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;

public abstract class AbstractOpenReportItemSpec implements
		Specification<ReportItem> {

	private static final String QTY = "quantity";
	private static final String ORDER_ITEM = "orderItem";
	CriteriaBuilder cb;
	CriteriaQuery<?> query;
	Root<ReportItem> root;

	/**
	 * see http://stackoverflow.com/questions/3997070/jpa-criteria-tutorial
	 */
	@Override
	public Predicate toPredicate(Root<ReportItem> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		this.cb = cb;
		this.query = query;
		this.root = root;
		
		Subquery<ReportItem> subquery = query.subquery(ReportItem.class);
		Root<ReportItem> fromSubquery = subquery.from(ReportItem.class);
		subquery.where(cb.and(
				cb.equal(root.<OrderItem>get(ORDER_ITEM), fromSubquery.get(ORDER_ITEM)),
				cb.greaterThan(
						createQtySum(getReportItemClassToRetrieve()), 
						createQtySum(getReportItemClassToSubtract())
				)));
		subquery.select(fromSubquery);

		Predicate inCondition = cb.and(cb.exists(subquery), cb.equal(root.type(), ConfirmationItem.class));
		return inCondition;
	}

	private Subquery<Integer> createQtySum(
			Class<? extends ReportItem> specificReportItem) {
		Subquery<Integer> seSq = query.subquery(Integer.class);
		Root<? extends ReportItem> fromSi = seSq.from(specificReportItem);
		Expression<Integer> select = cb.<Integer>sum(fromSi.<Integer>get(QTY));
		Expression<Integer> coalescted = cb.coalesce(select, new Integer(0));
		seSq.select(coalescted);
		seSq.where(cb.equal(root.get(ORDER_ITEM), fromSi.get(ORDER_ITEM)));
		return seSq;
	}

	abstract Class<? extends ReportItem> getReportItemClassToRetrieve();
	
	abstract Class<? extends ReportItem> getReportItemClassToSubtract();
}
