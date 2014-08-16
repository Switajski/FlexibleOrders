package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;

public class InvoiceItemBuilder extends ReportItemBuilder<InvoiceItem, Builder<InvoiceItem>> {

	@Override
	public InvoiceItem build() {
		InvoiceItem ii = new InvoiceItem();
		super.build(ii);
		return ii;
	}
}
