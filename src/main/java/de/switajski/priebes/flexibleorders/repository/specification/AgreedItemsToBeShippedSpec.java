package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

public class AgreedItemsToBeShippedSpec extends AbstractOpenReportItemSpecification{

    public AgreedItemsToBeShippedSpec() {
        super(ConfirmationItem.class, ShippingItem.class);
    }
    
}
