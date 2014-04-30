package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class InvoiceItem extends ReportItem {

	protected InvoiceItem() {}
	
	public InvoiceItem(Invoice invoice, ReportItemType invoice2,
			OrderItem orderItem, Integer quantityToDeliver, Date date) {
		super(invoice, invoice2, orderItem, quantityToDeliver, date);
	}

}
