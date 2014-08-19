package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

public class ToBeAgreedSpec extends AbstractOpenReportItemSpecification {

	@Override
	Class<? extends ReportItem> getReportItemClassToSubtract() {
		return ShippingItem.class;
	}

}
