package de.switajski.priebes.flexibleorders.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public abstract class AddressFromPurchaseAgreementRetriever {

    abstract Address getAddress(PurchaseAgreement purchaseAgreement);

    @Transactional(readOnly = true)
    public Set<Address> retrieve(ReportItem reportItem) {
        Set<Address> addresses = new HashSet<Address>();
        if (reportItem.getReport() instanceof OrderConfirmation) {
            // a purchase agreement only exists after OrderAgreement
            PurchaseAgreement pa = ((OrderConfirmation) reportItem.getReport()).getPurchaseAgreement();
            addresses.add(getAddress(pa));
        }
        else {// TODO: deprecated and buggy retrieval method, after successors
              // and predecessors of an report item were introduced...
            DeliveryHistory dh = DeliveryHistory.of(reportItem.getOrderItem());
            for (ConfirmationItem ai : dh.reportItems(ConfirmationItem.class)) {
                try {
                    PurchaseAgreement purchaseAgreement = ((OrderConfirmation) ai.getReport()).getPurchaseAgreement();
                    addresses.add(getAddress(purchaseAgreement));
                }
                catch (ClassCastException e) {
                    throw new RuntimeException("AgreementItem expected to have OrderAgreement as Report", e);
                }
            }
        }
        return addresses;
    }

    @Transactional(readOnly = true)
    public Set<Address> retrieve(Collection<ReportItem> reportItems) {
        Set<Address> addresses = new HashSet<Address>();
        for (ReportItem ri : reportItems) {
            addresses.addAll(retrieve(ri));
        }
        return addresses;
    }

}
