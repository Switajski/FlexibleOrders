package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

public class ShippingItemToBeInvoicedSpec extends AbstractOpenReportItemSpecification {

    @Override
    Class<? extends ReportItem> getReportItemClassToRetrieve() {
        return ShippingItem.class;
    }
    
	@Override
	Class<? extends ReportItem> getReportItemClassToSubtract() {
		return InvoiceItem.class;
	}

}
