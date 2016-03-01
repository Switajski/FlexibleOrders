package de.switajski.priebes.flexibleorders.validation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementReadService;
import de.switajski.priebes.flexibleorders.service.conversion.ItemDtoToReportItemConversionService;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

/**
 * An invoice is not consistent if the parameter has items differing invoicing
 * addresses in the purchase agreement.
 * 
 * @author switajski
 *
 */
@Component
public class ConsistentInvoicingAddressValidator implements ConstraintValidator<ConsistentInvoicingAddress, Collection<ItemDto>> {

    @Autowired
    PurchaseAgreementReadService purchaseAgreementReadService;

    @Autowired
    ItemDtoToReportItemConversionService id2riConverter;

    @Override
    public void initialize(ConsistentInvoicingAddress constraintAnnotation) {}

    @Override
    public boolean isValid(Collection<ItemDto> items, ConstraintValidatorContext context) {
        if (items.isEmpty()) return true;

        Map<ReportItem, Integer> ris = id2riConverter.mapItemDtosToReportItemsWithQty(items);
        Set<Address> invoiceAddresses = purchaseAgreementReadService.invoiceAddresses(ris.keySet());

        if (1 < invoiceAddresses.size()) {
            return false;
        }

        return true;
    }

}
