package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ReportItemType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Report;

public class HandlingEventBuilder implements Builder<ReportItem> {

	private Integer quantity;
	private Report report = null;
	private ReportItemType type;
	private OrderItem item;

	public HandlingEventBuilder(ReportItemType type, OrderItem item,
			Integer quantity) {
		this.type = type;
		this.item = item;
		this.quantity = quantity;
	}

	@Override
	public ReportItem build() {
		ReportItem he = new ReportItem(report, type, item, quantity, null);
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

	public HandlingEventBuilder setType(ReportItemType type) {
		this.type = type;
		return this;
	}

	public HandlingEventBuilder setItem(OrderItem item) {
		this.item = item;
		return this;
	}

}
