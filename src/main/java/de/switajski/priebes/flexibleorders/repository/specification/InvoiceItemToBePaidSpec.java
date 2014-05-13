package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;

public class InvoiceItemToBePaidSpec extends AbstractOpenReportItemSpec {

	@Override
	Class<? extends ReportItem> getReportItemClassToRetrieve() {
		return InvoiceItem.class;
	}

	@Override
	Class<? extends ReportItem> getReportItemClassToSubtract() {
		return ReceiptItem.class;
	}

}
