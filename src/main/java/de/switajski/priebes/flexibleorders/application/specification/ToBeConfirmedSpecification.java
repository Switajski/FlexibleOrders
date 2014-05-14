package de.switajski.priebes.flexibleorders.application.specification;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class ToBeConfirmedSpecification extends ItemSpecification {

	@Override
	public boolean isSatisfiedBy(OrderItem item) {
		if (!item.getDeliveryHistory().getCancellationItems().isEmpty()) return false;
		return !(new ConfirmedSpecification().isSatisfiedBy(item));
	}
	
}
