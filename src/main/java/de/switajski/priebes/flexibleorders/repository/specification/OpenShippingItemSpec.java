package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class OpenShippingItemSpec extends AbstractOpenReportItemSpecification {

	@Override
	Class<? extends ReportItem> getReportItemClassToSubtract() {
		return AgreementItem.class;
	}

}
