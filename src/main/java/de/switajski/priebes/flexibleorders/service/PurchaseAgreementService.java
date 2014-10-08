package de.switajski.priebes.flexibleorders.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

@Service
public class PurchaseAgreementService {

    @Transactional(readOnly = true)
    public Set<PurchaseAgreement> retrieve(Collection<ReportItem> reportItems) {
        DeliveryHistory dh = new DeliveryHistory(reportItems);
        Set<PurchaseAgreement> ocs = new HashSet<PurchaseAgreement>();
        for (ConfirmationItem cis : dh.getReportItems(ConfirmationItem.class)) {
            OrderConfirmation orderConfirmation = (OrderConfirmation) cis.getReport();
            ocs.add(orderConfirmation.getPurchaseAgreement());
        }
        return ocs;
    }

    /**
     * retrieve only purchase agreements that are agreed by an order agreement
     * 
     * @param reportItems
     * @return
     */
    @Transactional(readOnly = true)
    public Set<PurchaseAgreement> retrieveLegal(Collection<ReportItem> reportItems) {
        DeliveryHistory dh = new DeliveryHistory(reportItems);
        Set<PurchaseAgreement> ocs = new HashSet<PurchaseAgreement>();
        for (ConfirmationItem cis : dh.getReportItems(ConfirmationItem.class)) {
            OrderConfirmation orderConfirmation = (OrderConfirmation) cis.getReport();
            if (orderConfirmation.isAgreed()) {
                ocs.add(orderConfirmation.getPurchaseAgreement());
            }
        }
        return ocs;
    }

}
