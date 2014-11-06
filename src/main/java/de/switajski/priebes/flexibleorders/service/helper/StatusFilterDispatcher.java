package de.switajski.priebes.flexibleorders.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.specification.AgreedItemsToBeShippedSpec;
import de.switajski.priebes.flexibleorders.repository.specification.ConfirmationItemToBeAgreedSpec;
import de.switajski.priebes.flexibleorders.repository.specification.InvoiceItemToBePaidSpec;
import de.switajski.priebes.flexibleorders.repository.specification.IssuedItemSpec;
import de.switajski.priebes.flexibleorders.repository.specification.ReceiptItemCompletedSpec;
import de.switajski.priebes.flexibleorders.repository.specification.ShippingItemToBeInvoicedSpec;
import de.switajski.priebes.flexibleorders.web.helper.ProductionState;

@Service
public class StatusFilterDispatcher {

    @Autowired
    private CustomerRepository customerRepo;

    public Specification<ReportItem> dispatchStatus(ProductionState processStep) {
        if (processStep == null) throw new IllegalArgumentException("Status nicht angegeben");
        
        switch (processStep) {
            case CONFIRMED:
                return new ConfirmationItemToBeAgreedSpec();

            case AGREED:
                return new AgreedItemsToBeShippedSpec();

            case SHIPPED:
                return new ShippingItemToBeInvoicedSpec();

            case INVOICED:
                return new InvoiceItemToBePaidSpec();

            case COMPLETED:
                return new ReceiptItemCompletedSpec();

            case ISSUED:
                return new IssuedItemSpec();
            
            case DELIVERED:
                return new ShippingItemToBeInvoicedSpec(); 

        }
        throw new NotFoundException("Status "+ processStep +"nicht gefunden");

    }

}
