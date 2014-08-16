package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;

public class ReceiptItemBuilder extends ReportItemBuilder<ReceiptItem, Builder<ReceiptItem>>
		implements Builder<ReceiptItem> {

	public ReceiptItem build() {
		ReceiptItem ii = new ReceiptItem();
		super.build(ii);
		return ii;
	}

}
