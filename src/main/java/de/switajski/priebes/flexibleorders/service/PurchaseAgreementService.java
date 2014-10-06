package de.switajski.priebes.flexibleorders.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;

@Service
public class PurchaseAgreementService {
    
    @Transactional(readOnly = true)
    public PurchaseAgreement retrieveOne(Collection<ReportItem> reportItems){
        Set<PurchaseAgreement> purchaseAgreements = retrieve(reportItems);
        if (purchaseAgreements.isEmpty())
            throw new IllegalStateException("Konnte keine Kaufvertr"+Unicode.aUml+"ge finden");
        if (!hasEqualPurchaseAgreements(reportItems)){
            throw new ContradictoryPurchaseAgreementException(purchaseAgreements);
        }
        return purchaseAgreements.iterator().next();
    }
    
    @Transactional(readOnly = true)
    public boolean hasEqualPurchaseAgreements(Collection<ReportItem> reportItems){
        Set<PurchaseAgreement> ocs = retrieve(reportItems);
        
        if (ocs.size() > 1)
            return false;
        
        return true;
    }

    @Transactional(readOnly = true)
    public Set<PurchaseAgreement> retrieve(Collection<ReportItem> reportItems) {
        DeliveryHistory dh = new DeliveryHistory(reportItems);
        Set<PurchaseAgreement> ocs = new HashSet<PurchaseAgreement>(); 
        for (AgreementItem cis: dh.getItems(AgreementItem.class))
            ocs.add(((OrderAgreement) cis.getReport()).getPurchaseAgreement());
        return ocs;
    }


}
