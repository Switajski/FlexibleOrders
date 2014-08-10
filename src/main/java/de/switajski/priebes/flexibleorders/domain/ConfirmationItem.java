package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Entity;

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
