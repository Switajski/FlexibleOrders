package de.switajski.priebes.flexibleorders.application;

import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class QuantityLeftCalculator {

	private OrderItem orderItem;
	private ReportItem reportItem;

	public Integer calculate(OrderItem orderItem){
		return calculate(orderItem, ReportItemType.CONFIRM);
	}
	
	public Integer calculate(OrderItem orderItem, ReportItemType type) {
		this.orderItem = orderItem;
		return calculateQuantityLeft(type);
	}
	
	public Integer calculate(ReportItem reportItem, ReportItemType type){
		this.reportItem = reportItem;
		this.orderItem = reportItem.getOrderItem();
		return calculateQuantityLeft(type);
	}
	
	/**
	 * Provides the quantity stuck in given {@link ReportItemType}.
	 * @param type HandlingEventType representing a state
	 * @return quantity left in given {@link ReportItemType}
	 * @see ItemDto#getQuantityLeft
	 */
	private int calculateQuantityLeft(ReportItemType type) {
		int quantityLeft = 0;
		switch (type) {
		case ORDER:
			quantityLeft = orderItem.getOrderedQuantity() - getHandledQuantity(ReportItemType.CONFIRM);
			break;
		case CONFIRM:
			quantityLeft = orderItem.getOrderedQuantity() - getHandledQuantity(ReportItemType.SHIP);
			break;
		case SHIP:
			quantityLeft = getHandledQuantity(ReportItemType.SHIP) 
					- getHandledQuantity(ReportItemType.INVOICE);
			break;
		case INVOICE:
			quantityLeft = getHandledQuantity(ReportItemType.INVOICE) 
					- getHandledQuantity(ReportItemType.PAID);
			break;
		case PAID:
			quantityLeft = getHandledQuantity(ReportItemType.PAID);
//					- getHandledQuantity(HandlingEventType.INVOICE);
			break;
		case CANCEL:
			break;
		case FORWARD_TO_THIRD_PARTY:
			break;
		default:
			break;
		}
		return quantityLeft;
	}
	
	/**
	 * returns the summed quantity of all HandlingEvents of given HandlingEventType
	 * 
	 * @return null if no HandlingEvents of given type found
	 */
	private Integer getHandledQuantity(ReportItemType type) {
		Set<ReportItem> hes = getAllHesOfType(type);
		if (hes.isEmpty()) return 0;
		int summed = 0;
		for (ReportItem he: hes){
			summed = summed + he.getQuantity();
		}
		return summed;
	}
	
	private Set<ReportItem> getAllHesOfType(ReportItemType type) {
		Set<ReportItem> hesOfType = new HashSet<ReportItem>();
		for (ReportItem he: orderItem.getReportItems()){
			if (he.getType() == type)
				hesOfType.add(he);
		}
		return hesOfType;
	}

}
