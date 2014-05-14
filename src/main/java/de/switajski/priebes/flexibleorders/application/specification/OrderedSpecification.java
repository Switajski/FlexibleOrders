package de.switajski.priebes.flexibleorders.application.specification;

import de.switajski.priebes.flexibleorders.domain.OrderItem;


/**
 * 
 * @author Marek Switajski
 *
 */
public class OrderedSpecification extends ItemSpecification{

	public boolean isSatisfiedBy(OrderItem item){
		if (item.getOrderedQuantity() == null || 
				item.getOrderedQuantity() < 0) return false;
		if (item.getOrder().getOrderNumber() == null) return false;
		if (item.getProduct().getName() == null) return false;
		if (item.getProduct().getProductNumber() == null) return false;
		if (item.getOrder().getCustomerEmail() == null
				&& item.getOrder().getCustomer() == null) return false;
		if (!item.getDeliveryHistory().getCancellationItems().isEmpty()) return false;
		
		return true;
	}
	
}
