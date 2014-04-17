package de.switajski.priebes.flexibleorders.application.specification;

import javax.persistence.Embeddable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

/**
 * Defines and validates an item in a confirmed state.</br>
 *   
 * @author Marek Switajski
 *
 */
@Embeddable
public class ConfirmedSpecification extends ItemSpecification{

	private Boolean sendOrderConfirmationEmail;
	
	private Boolean sendOrderConfirmationLetter;
	
	protected ConfirmedSpecification() {}

	/**
	 * 
	 * 
	 * @param sendEmail
	 * @param sendLetter
	 */
	public ConfirmedSpecification( 
			boolean sendEmail,
			boolean sendLetter
			) {
		this.sendOrderConfirmationEmail = sendEmail;
		this.sendOrderConfirmationLetter = sendLetter;
	}
	
	public boolean isSatisfiedBy(final OrderItem item){
		//TODO: take account to executed tasks (includeTaskExecuted) 
		if (item.getDeliveryHistory().isEmpty()) return false;
		if (!item.getAllHesOfType(ReportItemType.CANCEL).isEmpty()) return false;
		for (ReportItem he: item.getAllHesOfType(ReportItemType.CONFIRM)){
			if (he.getReport() == null || he.getConfirmationReport() == null ||
					he.getConfirmationReport().getInvoiceAddress() == null ||
					//TODO: check VAT_RATE
					item.getNegotiatedPriceNet() == null ||
					item.getOrder().getCustomer() == null) 
				return false;
		}
		
		int confirmedQuantity = this.getHandledQuantityFromEvents(item, ReportItemType.CONFIRM);
		if (confirmedQuantity != item.getOrderedQuantity()) return false;
		return true;
	}
	
	@Override
	public Predicate toPredicate(Root<OrderItem> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		SetJoin<OrderItem, ReportItem> heJoin = root.joinSet("deliveryHistory");
		
		Predicate confirmedPred = cb.and(
				cb.isNotNull(root.<Order>get("flexibleOrder")),
				cb.equal(heJoin.<ReportItemType>get("type"), cb.literal(ReportItemType.CONFIRM)),
				cb.isNotNull(heJoin.get("report")),
				//TODO add deliveryNotes check
//				cb.greaterThan(root.get("flexibleOrder").get("orderedQuantity").as(Integer.class), 0),
				cb.isNotNull(root.get("product").get("name")),
				cb.isNotNull(root.get("product").get("productNumber")),
				cb.isNotNull(root.get("negotiatedPriceNet"))
				);
		return confirmedPred;
	}

	public boolean isSendEmail() {
		return sendOrderConfirmationEmail;
	}

	public void setSendEmail(boolean sendEmail) {
		this.sendOrderConfirmationEmail = sendEmail;
	}

	public boolean isSendLetter() {
		return sendOrderConfirmationLetter;
	}

	public void setSendLetter(boolean sendLetter) {
		this.sendOrderConfirmationLetter = sendLetter;
	}

}
