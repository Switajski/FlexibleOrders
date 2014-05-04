package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import java.util.Date;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Report;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class ReportItemBuilder<T extends ReportItemBuilder> {

	protected Integer quantity;
	protected Report report = null;
	protected OrderItem item;
	protected Date date;

	
	public T setQuantity(Integer quantity) {
		this.quantity = quantity;
		return (T) this;
	}

	public T setReport(Report report) {
		this.report = report;
		return (T) this;
	}

	public T setItem(OrderItem item) {
		this.item = item;
		return (T) this;
	}

	public T setDate(Date date) {
		this.date = date;
		return (T) this;
	}

}
