package de.switajski.priebes.flexibleorders.domain.report;

import java.util.Date;

import javax.persistence.Entity;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Entity
public class ConfirmationItem extends ReportItem {

	public ConfirmationItem() {
	}

	public ConfirmationItem(OrderConfirmation cr, 
			OrderItem oi, Integer quantityLeft, Date date) {
		super(cr, oi, quantityLeft, date);
	}

	@Override
	public String provideStatus() {
		return "best&auml;tigt";
	}

}
