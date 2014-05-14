package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

public class ConfirmationItemToBeShippedSpec extends AbstractOpenReportItemSpec {

	@Override
	Class<? extends ReportItem> getReportItemClassToRetrieve() {
		return ConfirmationItem.class;
	}

	@Override
	Class<? extends ReportItem> getReportItemClassToSubtract() {
		return ShippingItem.class;
	}

}
