package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

public class ShippingItemToBeInvoicedSpec extends AbstractOpenReportItemSpec {

	@Override
	Class<? extends ReportItem> getReportItemClassToRetrieve() {
		return ShippingItem.class;
	}

	@Override
	Class<? extends ReportItem> getReportItemClassToSubtract() {
		return InvoiceItem.class;
	}

}
