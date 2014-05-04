package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class InvoiceItem extends ReportItem {

	protected InvoiceItem() {
	}

	public InvoiceItem(Invoice invoice, 
			OrderItem orderItem, Integer quantityToDeliver, Date date) {
		super(invoice, ReportItemType.INVOICE, orderItem, quantityToDeliver, date);
	}

	@Override
	public String provideStatus() {
		return "in Rechnung gestellt";
	}

}
