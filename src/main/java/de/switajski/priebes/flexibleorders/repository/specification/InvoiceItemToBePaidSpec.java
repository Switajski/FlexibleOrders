package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;

public class InvoiceItemToBePaidSpec extends AbstractOpenReportItemSpecification {

    public InvoiceItemToBePaidSpec() {
        super(InvoiceItem.class, ReceiptItem.class);
    }
    
}
