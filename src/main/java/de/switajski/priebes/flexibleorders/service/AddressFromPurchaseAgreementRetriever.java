package de.switajski.priebes.flexibleorders.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public abstract class AddressFromPurchaseAgreementRetriever {

    abstract Address retrieveAddressFromPurchaseAgreement(PurchaseAgreement purchaseAgreement);
    
    @Transactional(readOnly = true)
    public Set<Address> retrieve(ReportItem reportItem) {
        Set<Address> addresses = new HashSet<Address>();
        DeliveryHistory dh = DeliveryHistory.of(reportItem.getOrderItem());
        for (AgreementItem ai : dh.getItems(AgreementItem.class)) {
            try {
                PurchaseAgreement purchaseAgreement = ((OrderAgreement) ai.getReport()).getPurchaseAgreement();
                addresses.add(retrieveAddressFromPurchaseAgreement(purchaseAgreement));
            }
            catch (ClassCastException e) {
                throw new RuntimeException("AgreementItem expected to have OrderAgreement as Report", e);
            }
        }
        return addresses;
    }

    @Transactional(readOnly = true)
    public Set<Address> retrieve(Set<ReportItem> reportItems){
        Set<Address> addresses = new HashSet<Address>();
        for (ReportItem ri:reportItems){
            addresses.addAll(retrieve(ri));
        }
        return addresses; 
    }
    
}
