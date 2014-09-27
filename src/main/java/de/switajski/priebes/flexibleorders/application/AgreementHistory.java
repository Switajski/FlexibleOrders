package de.switajski.priebes.flexibleorders.application;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.exceptions.BusinessInputException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;

/**
 * @deprecated use DeliveryHistory instead
 * @author Marek Switajski
 *
 */
public class AgreementHistory {

    private Collection<AgreementItem> agreementItems;

    /**
     * @deprecated use {@link #AgreementHistory(Collection)} instead
     * @param deliveryHistory
     */
    public AgreementHistory(DeliveryHistory deliveryHistory){
        this.agreementItems = deliveryHistory.getItems(AgreementItem.class);
    }
    
    public AgreementHistory(Collection<AgreementItem> agreementItems){
        this.agreementItems = agreementItems;
    }
    
    public boolean hasEqualPurchaseAgreements(){
        Set<PurchaseAgreement> ocs = new HashSet<PurchaseAgreement>(); 
        for (AgreementItem cis: agreementItems)
            ocs.add(((OrderAgreement) cis.getReport()).getPurchaseAgreement());
        
        if (ocs.size() > 1)
            return false;
        
        return true;
    }
    
    /**
     * @deprecated use {@link #hasEqualPurchaseAgreements()} and fail in logic
     * @return
     */
    public PurchaseAgreement retrieveOnePurchaseAgreementOrFail(){
        Set<PurchaseAgreement> ocs = new HashSet<PurchaseAgreement>(); 
        for (AgreementItem cis: agreementItems)
            ocs.add(((OrderAgreement) cis.getReport()).getPurchaseAgreement());
        
        if (!hasEqualPurchaseAgreements())
            throw new BusinessInputException("Unterschiedliche Kaufvertr"+Unicode.aUml+"ge vorhanden");
        return getOneOrNullIfEmptyOrFail(ocs);
    }
    
    public boolean hasEqualCustomerDetails(){
        Set<CustomerDetails> customerDetails = new HashSet<CustomerDetails>();
        for (AgreementItem si:agreementItems){
            customerDetails.add(((OrderAgreement) si.getReport()).getCustomerDetails());
        }
        
        if (customerDetails.size() > 1)
            return false;
        return true;
    }

    /**
     * @deprecated use {@link #hasEqualCustomerDetails()}
     * @return
     */
    public CustomerDetails getOneCustomerDetailOrFail(){
        Set<CustomerDetails> cds = new HashSet<CustomerDetails>();
        for (AgreementItem si:agreementItems){
            cds.add(((OrderAgreement) si.getReport()).getCustomerDetails());
        }
        return getOneOrNullIfEmptyOrFail(cds);
    }
    
    private <T> T getOneOrNullIfEmptyOrFail(Collection<T> collection){
        if (collection.size() < 1)
            return null;
        else if (collection.size() == 1)
            return collection.iterator().next();
        else {
            throw new IllegalStateException("Widersprechende Daten aus Auftr"+Unicode.aUml+"gen gefunden");
        }
    }
    
}
