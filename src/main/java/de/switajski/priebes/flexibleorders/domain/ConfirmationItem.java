package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class ConfirmationItem extends ReportItem {

	protected ConfirmationItem() {
	}

	public ConfirmationItem(ConfirmationReport cr, 
			OrderItem oi, Integer quantityLeft, Date date) {
		super(cr, ReportItemType.CONFIRM, oi, quantityLeft, date);
	}

	@Override
	public String provideStatus() {
		return "best&auml;tigt";
	}

}
