package de.switajski.priebes.flexibleorders.application.specification;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class PaidSpecification extends ItemSpecification {

	@Override
	public boolean isSatisfiedBy(OrderItem item) {
		DeliveryHistory deliveryHistory = item.getDeliveryHistory();
		if (deliveryHistory.isEmpty()) return false;
		if (!deliveryHistory.getCancellationItems().isEmpty()) return false;
		if (deliveryHistory.getReceiptItems().isEmpty())
			return false;
		else return true;
	}

}
