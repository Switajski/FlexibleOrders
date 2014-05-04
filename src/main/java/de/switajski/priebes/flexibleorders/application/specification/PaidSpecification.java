package de.switajski.priebes.flexibleorders.application.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;

public class PaidSpecification extends ItemSpecification {

	@Override
	public Predicate toPredicate(Root<OrderItem> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSatisfiedBy(OrderItem item) {
		if (item.getReportItems().isEmpty()) return false;
		if (!item.getAllHesOfType(ReportItemType.CANCEL).isEmpty()) return false;
		if (item.getAllHesOfType(ReportItemType.PAID).isEmpty())
			return false;
		else return true;
	}

}
