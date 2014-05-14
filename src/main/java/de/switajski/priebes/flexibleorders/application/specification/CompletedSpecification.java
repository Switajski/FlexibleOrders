package de.switajski.priebes.flexibleorders.application.specification;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class CompletedSpecification extends ItemSpecification{

	@Override
	public boolean isSatisfiedBy(OrderItem item) {
		return (new ShippedSpecification().isSatisfiedBy(item) && new PaidSpecification().isSatisfiedBy(item));
	}

}
