package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class IssuedItemSpec extends AbstractOpenReportItemSpecification {

    @Override
    Class<? extends ReportItem> getReportItemClassToRetrieve() {
        return InvoiceItem.class;
    }
    
	@Override
	Class<? extends ReportItem> getReportItemClassToSubtract() {
		return ReceiptItem.class;
	}
	
}
