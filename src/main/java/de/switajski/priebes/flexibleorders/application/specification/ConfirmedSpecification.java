package de.switajski.priebes.flexibleorders.application.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import de.switajski.priebes.flexibleorders.application.QuantityLeftCalculator;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;

/**
 * Defines and validates an item in a confirmed state.</br>
 *   
 * @author Marek Switajski
 *
 */
public class ConfirmedSpecification extends ItemSpecification{

	public boolean isSatisfiedBy(final OrderItem item){
		//TODO: take account to executed tasks (includeTaskExecuted) 
		if (item.getReportItems().isEmpty()) return false;
		if (!item.getDeliveryHistory().getCancellationItems().isEmpty()) return false;
		for (ReportItem he: item.getDeliveryHistory().getConfirmationItems()){
			if (he.getReport() == null || ((ConfirmationReport) he.getReport()).getInvoiceAddress() == null ||
					//TODO: check VAT_RATE
					item.getNegotiatedPriceNet() == null ||
					item.getOrder().getCustomer() == null) 
				return false;
		}
		if (new QuantityLeftCalculator().toBeShipped(item.getDeliveryHistory()) == 0)
			return true;
		return false;
	}
	
	@Override
	public Predicate toPredicate(Root<OrderItem> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		SetJoin<OrderItem, ReportItem> heJoin = root.joinSet("reportItems");
		
		Predicate confirmedPred = cb.and(
				cb.isNotNull(root.<Order>get("flexibleOrder")),
//				cb.equal(heJoin.<ReportItemType>get("type"), cb.literal(ReportItemType.CONFIRM)),
				cb.isNotNull(heJoin.get("report")),
				//TODO add deliveryNotes check
//				cb.greaterThan(root.get("flexibleOrder").get("orderedQuantity").as(Integer.class), 0),
				cb.isNotNull(root.get("product").get("name")),
				cb.isNotNull(root.get("product").get("productNumber")),
				cb.isNotNull(root.get("negotiatedPriceNet"))
				);
		return confirmedPred;
	}

}
