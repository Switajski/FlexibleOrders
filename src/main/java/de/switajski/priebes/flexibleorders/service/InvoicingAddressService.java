package de.switajski.priebes.flexibleorders.service;

import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;

@Service
public class InvoicingAddressService extends AddressFromPurchaseAgreementRetriever{

    @Override
    Address getAddress(PurchaseAgreement purchaseAgreement) {
        return purchaseAgreement.getInvoiceAddress();
    }
    
}
