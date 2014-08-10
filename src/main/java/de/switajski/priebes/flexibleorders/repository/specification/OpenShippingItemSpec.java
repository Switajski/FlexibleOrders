package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

public class OpenShippingItemSpec extends AbstractOpenReportItemSpec {

	@Override
	Class<? extends ReportItem> getReportItemClassToSubtract() {
		return ShippingItem.class;
	}

}
