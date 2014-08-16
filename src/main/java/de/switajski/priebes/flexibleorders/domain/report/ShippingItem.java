package de.switajski.priebes.flexibleorders.domain.report;

import java.util.Date;

import javax.persistence.Entity;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Entity
public class ShippingItem extends ReportItem {

	public ShippingItem() {
	}

	public ShippingItem(DeliveryNotes deliveryNotes,
			OrderItem orderItemToBeDelivered, Integer quantityToDeliver,
			Date date) {
		super(
				deliveryNotes,
				orderItemToBeDelivered,
				quantityToDeliver,
				date);
	}

	@Override
	public String provideStatus() {
		return "geliefert";
	}

	public DeliveryNotes getDeliveryNotes() {
		return (DeliveryNotes) this.report;
	}

}
