package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

public class AgreementItemToBeShippedSpec extends AbstractOpenReportItemSpecification {

    @Override
    Class<? extends ReportItem> getReportItemClassToRetrieve() {
        return AgreementItem.class;
    }

    @Override
    Class<? extends ReportItem> getReportItemClassToSubtract() {
        return ShippingItem.class;
    }

}
