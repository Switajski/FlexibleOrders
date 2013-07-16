package de.switajski.priebes.flexibleorders.report;

import java.util.List;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;

public class Invoice extends Report<InvoiceItem> {

	public Invoice(List<InvoiceItem> items) {
		super(items);
	}

}
