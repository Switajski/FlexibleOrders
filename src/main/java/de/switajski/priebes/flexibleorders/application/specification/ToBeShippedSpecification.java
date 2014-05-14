package de.switajski.priebes.flexibleorders.application.specification;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class ToBeShippedSpecification extends ItemSpecification {

	public ToBeShippedSpecification() {	}
	
	@Override
	public boolean isSatisfiedBy(OrderItem item) {
		return (new ConfirmedSpecification().isSatisfiedBy(item) &&
				!(new ShippedSpecification().isSatisfiedBy(item)));
	}

}
