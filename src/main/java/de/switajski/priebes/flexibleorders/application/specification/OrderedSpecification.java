package de.switajski.priebes.flexibleorders.application.specification;

import javax.persistence.Embeddable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.switajski.priebes.flexibleorders.domain.ReportItemType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;


/**
 * 
 * @author Marek Switajski
 *
 */
@Embeddable
public class OrderedSpecification extends ItemSpecification{

	public boolean isSatisfiedBy(OrderItem item){
		if (item.getOrderedQuantity() == null || 
				item.getOrderedQuantity() < 0) return false;
		if (item.getOrder().getOrderNumber() == null) return false;
		if (item.getProduct().getName() == null) return false;
		if (item.getProduct().getProductNumber() == null) return false;
		if (item.getOrder().getCustomerEmail() == null
				&& item.getOrder().getCustomer() == null) return false;
		if (!item.getAllHesOfType(ReportItemType.CANCEL).isEmpty()) return false;
		
		return true;
	}
	
	@Override
	public Predicate toPredicate(Root<OrderItem> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		
		Predicate orderedPred = cb.and(
				cb.greaterThan(root.get("orderedQuantity").as(Integer.class), 0),
				cb.isNotNull(root.get("product").get("name")),
				cb.isNotNull(root.get("product").get("productNumber")),
				cb.isNotNull(root.get("flexibleOrder"))
				);
		return orderedPred;
	}

}
