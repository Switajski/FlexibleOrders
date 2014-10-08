package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

public class ShippingItemToBeInvoicedSpec extends AbstractOpenReportItemSpecification {

    public ShippingItemToBeInvoicedSpec() {
        super(ShippingItem.class, InvoiceItem.class);
    }
    
}
