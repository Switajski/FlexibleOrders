package de.switajski.priebes.flexibleorders.service.helper;

import static org.springframework.data.jpa.domain.Specifications.where;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.repository.specification.IsConfirmationItemSpecification;
import de.switajski.priebes.flexibleorders.repository.specification.IsInvoiceItemSpecification;
import de.switajski.priebes.flexibleorders.repository.specification.IsReceiptItemSpecification;
import de.switajski.priebes.flexibleorders.repository.specification.IsShippingItemSpecification;
import de.switajski.priebes.flexibleorders.repository.specification.OverdueItemSpecification;
import de.switajski.priebes.flexibleorders.web.helper.ProductionState;

@Service
public class StatusFilterDispatcher {

    public Specification<ReportItem> dispatchStatus(ProductionState processStep) {
        if (processStep == null) throw new IllegalArgumentException("Status nicht angegeben");

        OverdueItemSpecification overdueSpecification = new OverdueItemSpecification();
        switch (processStep) {
            case CONFIRMED:
                return where(new IsConfirmationItemSpecification())
                        .and(overdueSpecification);
            // return new ConfirmationItemToBeAgreedSpec();

            case AGREED:
                return where(new IsConfirmationItemSpecification())
                        .and(new OverdueItemSpecification());

            case SHIPPED:
                return where(new IsShippingItemSpecification())
                        .and(overdueSpecification);
            // return new ShippingItemToBeInvoicedSpec();

            case INVOICED:
                return where(new IsInvoiceItemSpecification())
                        .and(overdueSpecification);
            // return new InvoiceItemToBePaidSpecification();

            case COMPLETED:
                return new IsReceiptItemSpecification();

            case ISSUED:
                return where(new IsInvoiceItemSpecification())
                        .and(overdueSpecification);
            // return new InvoiceItemToBePaidSpecification();

            case DELIVERED:
                return where(new IsShippingItemSpecification())
                        .and(overdueSpecification);
            // return new ShippingItemToBeInvoicedSpec();

        }
        throw new NotFoundException("Status " + processStep + "nicht gefunden");

    }

}
