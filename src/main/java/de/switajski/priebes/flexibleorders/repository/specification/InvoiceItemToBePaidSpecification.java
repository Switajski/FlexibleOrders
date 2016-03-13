package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class InvoiceItemToBePaidSpecification extends AbstractOpenReportItemSpecification implements java.util.function.Predicate<ReportItem> {

    public InvoiceItemToBePaidSpecification() {
        super(InvoiceItem.class, ReceiptItem.class);
    }

    @Override
    public boolean test(ReportItem ri) {
        if (!(ri instanceof InvoiceItem)) return false;
        return ((InvoiceItem) ri).isOverdue();
    }

}
