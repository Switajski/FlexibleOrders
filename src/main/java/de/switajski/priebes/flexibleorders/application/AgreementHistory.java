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

    public AgreementHistory(Collection<AgreementItem> agreementItems) {
        this.agreementItems = agreementItems;
    }
    
    public AgreementDetails getAgreementDetails(){
        Set<AgreementDetails> ocs = new HashSet<AgreementDetails>(); 
        for (AgreementItem cis: agreementItems)
            ocs.add(((OrderAgreement) cis.getReport()).getAgreementDetails());
        if (ocs.size() < 1)
            return null;
        else if (ocs.size() == 1)
            return ocs.iterator().next();
        else {
            throw new IllegalStateException("Widersprechende Daten aus Auftr"+Unicode.aUml+"gen gefunden");
        }
    }
    
    public CustomerDetails getCustomerDetails(){
        CustomerDetails attribute = null;
        for (AgreementItem si:agreementItems){
            CustomerDetails a = ((OrderAgreement) si.getReport()).getCustomerDetails();
            if (attribute == null)
                attribute = a;
            else if (!a.equals(attribute))
                throw new IllegalStateException("Widersprechende Daten aus Auftr"+Unicode.aUml+"gen gefunden");
        }
        return attribute;
    }
}
