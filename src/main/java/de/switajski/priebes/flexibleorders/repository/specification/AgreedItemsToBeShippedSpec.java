package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

public class AgreedItemsToBeShippedSpec extends AbstractOpenReportItemSpecification implements java.util.function.Predicate<ReportItem> {

    public AgreedItemsToBeShippedSpec() {
        super(ConfirmationItem.class, ShippingItem.class);
    }

    @Override
    public boolean test(ReportItem ri) {
        if (!(ri instanceof ConfirmationItem)) {
            return false;
        }
        ConfirmationItem ci = (ConfirmationItem) ri;
        if (!ci.isAgreed()) return false;

        return ci.isOverdue();
    }

}
