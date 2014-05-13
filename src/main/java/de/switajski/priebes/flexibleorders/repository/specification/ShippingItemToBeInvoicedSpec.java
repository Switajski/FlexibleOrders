package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;

public class ShippingItemToBeInvoicedSpec extends AbstractOpenReportItemSpec {

	@Override
	Class<? extends ReportItem> getReportItemClassToRetrieve() {
		return ConfirmationItem.class;
	}

	@Override
	Class<? extends ReportItem> getReportItemClassToSubtract() {
		return ShippingItem.class;
	}

}
