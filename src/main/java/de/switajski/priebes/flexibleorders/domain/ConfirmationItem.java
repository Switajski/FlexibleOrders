package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class ConfirmationItem extends ReportItem{

	protected ConfirmationItem() {}
	
	public ConfirmationItem(ConfirmationReport cr, ReportItemType confirm,
			OrderItem oi, Integer quantityLeft, Date date) {
		super(cr, confirm, oi, quantityLeft, date);
	}

}
