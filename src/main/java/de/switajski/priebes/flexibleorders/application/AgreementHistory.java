package de.switajski.priebes.flexibleorders.application;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;

public class AgreementHistory {

    private Collection<AgreementItem> agreementItems;

    public AgreementHistory(DeliveryHistory deliveryHistory){
        this.agreementItems = deliveryHistory.getItems(AgreementItem.class);
    }
    
    public PurchaseAgreement retrieveOnePurchaseAgreementOrFail(){
        Set<PurchaseAgreement> ocs = getPuchaseAgreement();
        validateAgreementDetails(agreementItems);
        return getOneOrNullIfEmpty(ocs);
    }
    
    private void validateAgreementDetails(Collection<AgreementItem> agreementItems) {
        if (agreementItems.size() > 1)
            throw new IllegalStateException("Unterschiedliche Kaufvertr"+Unicode.aUml+"ge vorhanden");
    }

    public Set<PurchaseAgreement> getPuchaseAgreement() {
        Set<PurchaseAgreement> ocs = new HashSet<PurchaseAgreement>(); 
        for (AgreementItem cis: agreementItems)
            ocs.add(((OrderAgreement) cis.getReport()).getAgreementDetails());
        return ocs;
    }
    
    public CustomerDetails getCustomerDetails(){
        Set<CustomerDetails> cds = new HashSet<CustomerDetails>();
        for (AgreementItem si:agreementItems){
            cds.add(((OrderAgreement) si.getReport()).getCustomerDetails());
        }
        return getOneOrNullIfEmpty(cds);
    }
    
    public <T> T getOneOrNullIfEmpty(Collection<T> collection){
        if (collection.size() < 1)
            return null;
        else if (collection.size() == 1)
            return collection.iterator().next();
        else {
            throw new IllegalStateException("Widersprechende Daten aus Auftr"+Unicode.aUml+"gen gefunden");
        }
    }
    
}
