package de.switajski.priebes.flexibleorders.application;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.embeddable.AgreementDetails;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;

public class AgreementHistory {

    private Collection<AgreementItem> agreementItems;

    public AgreementHistory(DeliveryHistory deliveryHistory){
        this.agreementItems = deliveryHistory.getItems(AgreementItem.class);
    }
    
    public AgreementDetails getOneAgreementDetail(){
        Set<AgreementDetails> ocs = getAgreementDetails();
        return getOneOrNullIfEmpty(ocs);
    }

    public Set<AgreementDetails> getAgreementDetails() {
        Set<AgreementDetails> ocs = new HashSet<AgreementDetails>(); 
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
