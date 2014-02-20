package de.switajski.priebes.flexibleorders.domain.specification;

import javax.persistence.Embeddable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Embeddable
public class ShippedSpecification extends ItemSpecification {

	private Boolean sendInvoiceLetter;
	
	private Boolean sendInvoiceEmail;
	
	
	protected ShippedSpecification() {}
	
	public ShippedSpecification(boolean sendInvoiceLetter, boolean sendInvoiceEmail) {
		this.sendInvoiceEmail = sendInvoiceEmail;
		this.sendInvoiceLetter = sendInvoiceLetter;
	}
	
	@Override
	public boolean isSatisfiedBy(OrderItem item) {
		if (item.getDeliveryHistory().isEmpty()) return false;
		if (item.getAllHesOfType(HandlingEventType.SHIP).isEmpty()) return false;
		if (!item.getAllHesOfType(HandlingEventType.CANCEL).isEmpty()) return false;
		
		if (getHandledQuantityFromEvents(item, HandlingEventType.CONFIRM) <= 
				getHandledQuantityFromEvents(item, HandlingEventType.SHIP))
			return true;
		
		return false;
	}

	@Override
	public Predicate toPredicate(Root<OrderItem> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		SetJoin<OrderItem, HandlingEvent> heJoin = root.joinSet("deliveryHistory");
		
		Expression<Long> shippedQuantity = cb.count(heJoin.get("quantity"));
		Expression<Long> confirmedQuantity;
		
		Predicate shippedPred = cb.and(
				cb.isNotNull(root.<FlexibleOrder>get("flexibleOrder")),
				cb.equal(heJoin.<HandlingEventType>get("type"), 
						cb.literal(HandlingEventType.SHIP))
//						,cb.equal(shippedQuantity, 1) FEHLER: Aggregatfunktionen sind nicht in der WHERE-Klausel erlaubt
				);
		return shippedPred;
	}

	public boolean isSendInvoiceLetter() {
		return sendInvoiceLetter;
	}

	public void setSendInvoiceLetter(boolean sendInvoiceLetter) {
		this.sendInvoiceLetter = sendInvoiceLetter;
	}

	public boolean isSendInvoiceEmail() {
		return sendInvoiceEmail;
	}

	public void setSendInvoiceEmail(boolean sendInvoiceEmail) {
		this.sendInvoiceEmail = sendInvoiceEmail;
	}

}
