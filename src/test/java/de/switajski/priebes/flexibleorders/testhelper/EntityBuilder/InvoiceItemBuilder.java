package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;

public class InvoiceItemBuilder extends ReportItemBuilder<InvoiceItemBuilder> implements Builder<InvoiceItem>{

	@Override
	public InvoiceItem build() {
		InvoiceItem ii = new InvoiceItem((Invoice) report, type, item, quantity, date);
		ii.setQuantity(quantity);
		ii.setReport((Invoice) report);
		return ii;
	}
}
