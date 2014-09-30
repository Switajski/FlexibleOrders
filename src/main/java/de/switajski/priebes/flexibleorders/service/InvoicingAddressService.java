package de.switajski.priebes.flexibleorders.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

@Service
public class InvoicingAddressService {

    @Transactional(readOnly = true)
    public Set<Address> retrieve(ReportItem reportItem) {
        Set<Address> invoicingAddresses = new HashSet<Address>();
        DeliveryHistory dh = DeliveryHistory.of(reportItem.getOrderItem());
        for (AgreementItem ai : dh.getItems(AgreementItem.class)) {
            try {
                invoicingAddresses.add(((OrderAgreement) ai.getReport()).getPurchaseAgreement().getInvoiceAddress());
            }
            catch (ClassCastException e) {
                throw new RuntimeException("AgreementItem expected to have OrderAgreement as Report", e);
            }
        }
        return invoicingAddresses;
    }
    
    @Transactional(readOnly = true)
    public Set<Address> retrieve(Set<ReportItem> reportItems){
        Set<Address> invoicingAddresses = new HashSet<Address>();
        for (ReportItem ri:reportItems){
            invoicingAddresses.addAll(retrieve(ri));
        }
        return invoicingAddresses; 
    }
}
