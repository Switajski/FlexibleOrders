package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.report.Invoice;

/**
 * @author Marek Switajski
 *
 */
public class InvoiceBuilder extends ReportBuilder<Invoice, Builder<Invoice>> {

    @Override
    public Invoice build() {
        Invoice invoice = new Invoice(documentNumber, null);
        super.build(invoice);
        return invoice;
    }

}
