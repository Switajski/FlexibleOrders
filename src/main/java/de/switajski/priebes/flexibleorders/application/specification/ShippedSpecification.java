package de.switajski.priebes.flexibleorders.application.specification;

import javax.persistence.Embeddable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;

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
		if (item.getReportItems().isEmpty()) return false;
		if (item.getAllHesOfType(ReportItemType.SHIP).isEmpty()) return false;
		if (!item.getAllHesOfType(ReportItemType.CANCEL).isEmpty()) return false;
		
		if (getHandledQuantityFromEvents(item, ReportItemType.CONFIRM) <= 
				getHandledQuantityFromEvents(item, ReportItemType.SHIP))
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
				cb.isNotNull(root.<Order>get("flexibleOrder")),
				cb.equal(heJoin.<ReportItemType>get("type"), 
						cb.literal(ReportItemType.SHIP))
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
