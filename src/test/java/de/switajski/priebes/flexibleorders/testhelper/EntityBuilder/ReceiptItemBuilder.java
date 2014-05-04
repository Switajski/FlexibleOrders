package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.domain.ReceiptItem;

public class ReceiptItemBuilder extends ReportItemBuilder<ReceiptItemBuilder>
		implements Builder<ReceiptItem> {

	public ReceiptItem build() {
		ReceiptItem ii = new ReceiptItem(
				(Receipt) report,
				item,
				quantity,
				date);
		ii.setQuantity(quantity);
		ii.setReport((Receipt) report);
		return ii;
	}

}
