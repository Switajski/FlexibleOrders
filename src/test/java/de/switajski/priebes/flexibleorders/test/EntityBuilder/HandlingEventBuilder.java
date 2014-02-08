package de.switajski.priebes.flexibleorders.test.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Report;

public class HandlingEventBuilder implements Builder<HandlingEvent> {

	private Integer quantity;
	private Report report = null;
	private HandlingEventType type;
	private OrderItem item;
	
	public HandlingEventBuilder(HandlingEventType type, OrderItem item, Integer quantity) {
		this.type = type;
		this.item = item;
		this.quantity = quantity;
	}
	
	@Override
	public HandlingEvent build() {
		HandlingEvent he = new HandlingEvent(report, type, item, quantity, null);
		he.setQuantity(quantity);
		he.setReport(report);
		return he;
	}

	public HandlingEventBuilder setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	public HandlingEventBuilder setReport(Report report) {
		this.report = report;
		return this;
	}

	public HandlingEventBuilder setType(HandlingEventType type) {
		this.type = type;
		return this;
	}

	public HandlingEventBuilder setItem(OrderItem item) {
		this.item = item;
		return this;
	}

}
