package de.switajski.priebes.flexibleorders.service.helper;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.specification.AgreementItemToBeShippedSpec;
import de.switajski.priebes.flexibleorders.repository.specification.ConfirmationItemToBeAgreedSpec;
import de.switajski.priebes.flexibleorders.repository.specification.InvoiceItemToBePaidSpec;
import de.switajski.priebes.flexibleorders.repository.specification.IssuedItemSpec;
import de.switajski.priebes.flexibleorders.repository.specification.ReceiptItemCompletedSpec;
import de.switajski.priebes.flexibleorders.repository.specification.ShippingItemToBeInvoicedSpec;
import de.switajski.priebes.flexibleorders.web.helper.ProcessStep;

@Controller
public class StatusFilterDispatcher {

    static final String STATUS_STRING = "status";

    @Autowired
    private CustomerRepository customerRepo;

    public Specification<ReportItem> dispatchToSpecification(HashMap<String, String> filterMap) {
        validate(filterMap);
        return dispatchStatus(filterMap.get(STATUS_STRING));
    }

    private void validate(HashMap<String, String> filterMap) {
        if (filterMap.get(STATUS_STRING) == null) throw new IllegalArgumentException("Keinen Statusfilter angegeben");
    }

    public Specification<ReportItem> dispatchStatus(String status) {
        if (status == null) throw new IllegalArgumentException("Status nicht angegeben");
        if (status.equals("ordered")) throw new IllegalArgumentException(
                "Filter 'ordered' cannot be applied to Reports, but Orders");
        
        switch (ProcessStep.mapFromString(status)) {
            case CONFIRMED:
                return new ConfirmationItemToBeAgreedSpec();

            case AGREED:
                return new AgreementItemToBeShippedSpec();

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
        throw new NotFoundException("Status "+ status +"nicht gefunden");

    }

}
