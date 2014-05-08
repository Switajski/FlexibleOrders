package de.switajski.priebes.flexibleorders.application.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.application.QuantityLeftCalculator;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;

public class ShippedSpecification extends ItemSpecification {

	@Override
	public boolean isSatisfiedBy(OrderItem item) {
		DeliveryHistory deliveryHistory = item.getDeliveryHistory();
		if (deliveryHistory.isEmpty()) return false;
		if (deliveryHistory.getShippingItems().isEmpty()) return false;
		if (!deliveryHistory.getCancellationItems().isEmpty()) return false;
		
		if (new QuantityLeftCalculator().toBeShipped(deliveryHistory) == 0)
			return true;
		
		return false;
	}

	@Override
	public Predicate toPredicate(Root<OrderItem> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		SetJoin<OrderItem, ReportItem> heJoin = root.joinSet("reportItems");
		
		Expression<Long> shippedQuantity = cb.count(heJoin.get("quantity"));
		Expression<Long> confirmedQuantity;
		
		Predicate shippedPred = cb.and(
				cb.isNotNull(root.<Order>get("flexibleOrder"))
//				cb.equal(heJoin.<ReportItemType>get("type"), 
//						cb.literal(ReportItemType.SHIP))
////						,cb.equal(shippedQuantity, 1) FEHLER: Aggregatfunktionen sind nicht in der WHERE-Klausel erlaubt
				);
		return shippedPred;
	}

}
