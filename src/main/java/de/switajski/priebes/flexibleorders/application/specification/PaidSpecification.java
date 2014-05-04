package de.switajski.priebes.flexibleorders.application.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class PaidSpecification extends ItemSpecification {

	@Override
	public Predicate toPredicate(Root<OrderItem> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSatisfiedBy(OrderItem item) {
		DeliveryHistory deliveryHistory = item.getDeliveryHistory();
		if (deliveryHistory.isEmpty()) return false;
		if (!deliveryHistory.getCancellationItems().isEmpty()) return false;
		if (deliveryHistory.getReceiptItems().isEmpty())
			return false;
		else return true;
	}

}
