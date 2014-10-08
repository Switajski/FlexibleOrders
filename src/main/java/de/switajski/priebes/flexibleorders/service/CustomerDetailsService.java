package de.switajski.priebes.flexibleorders.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

@Service
public class CustomerDetailsService {

	@Autowired
	PurchaseAgreementService purchaseAgreementService;

    private Set<CustomerDetails> retrieve(ReportItem reportItem) {
        Set<CustomerDetails> customerDetailss = new HashSet<CustomerDetails>();
        DeliveryHistory dh = DeliveryHistory.of(reportItem.getOrderItem());
        for (ConfirmationItem ai : dh.getReportItems(ConfirmationItem.class)) {
            try {
                CustomerDetails customerDetails = ((OrderConfirmation) ai.getReport()).getCustomerDetails();
                customerDetailss.add(customerDetails);
            }
            catch (ClassCastException e) {
                throw new RuntimeException("System expected, that an AgreementItem has an OrderAgreement as Report", e);
            }
        }
        return customerDetailss;
    }

    @Transactional(readOnly = true)
    public Set<CustomerDetails> retrieve(Set<ReportItem> reportItems){
        Set<CustomerDetails> customerDetails = new HashSet<CustomerDetails>();
        for (ReportItem ri:reportItems){
            Report r = ri.getReport();
            if (r instanceof OrderConfirmation)
                customerDetails.add(((OrderConfirmation) r).getCustomerDetails());
        }
            
        for (ReportItem ri:reportItems){
            customerDetails.addAll(retrieve(ri));
        }
        return customerDetails; 
    }

}
