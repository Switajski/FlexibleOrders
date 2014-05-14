package de.switajski.priebes.flexibleorders.application.specification;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.application.QuantityLeftCalculator;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class ShippedSpecification extends ItemSpecification {

	@Override
	public boolean isSatisfiedBy(OrderItem item) {
		DeliveryHistory deliveryHistory = item.getDeliveryHistory();
		if (deliveryHistory.isEmpty()) return false;
		if (deliveryHistory.getShippingItems().isEmpty()) return false;
		if (!deliveryHistory.getCancellationItems().isEmpty()) return false;
		
		if (new QuantityLeftCalculator().toBeShipped(deliveryHistory) == 0)
			return true;
		
		return false;
	}

}
