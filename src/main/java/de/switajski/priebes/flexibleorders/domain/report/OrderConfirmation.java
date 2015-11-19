package de.switajski.priebes.flexibleorders.domain.report;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;

/**
 *
 * @author Marek Switajski
 *
 */
@Entity
public class OrderConfirmation extends Report {

    @Embedded
    private CustomerDetails customerDetails;

    @Embedded
    private PurchaseAgreement purchaseAgreement;

    private String orderAgreementNumber;

    public OrderConfirmation() {}

    /**
     *
     * @param orderConfirmationNumber
     * @param invoiceAddress
     * @param shippingAddress
     * @param confirmedSpec
     */
    public OrderConfirmation(
            String orderConfirmationNumber,
            Address invoiceAddress,
            Address shippingAddress) {
        super(orderConfirmationNumber);
        PurchaseAgreement pa = new PurchaseAgreement();
        pa.setInvoiceAddress(invoiceAddress);
        pa.setShippingAddress(shippingAddress);
        setPurchaseAgreement(pa);
    }

    public String getOrderAgreementNumber() {
        return orderAgreementNumber;
    }

    public void setOrderAgreementNumber(String orderAgreementNumber) {
        this.orderAgreementNumber = orderAgreementNumber;
    }

    public boolean isAgreed() {
        return orderAgreementNumber != null;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public CustomerDetails getCustomerDetails() {
        return this.customerDetails;
    }

    public PurchaseAgreement getPurchaseAgreement() {
        return purchaseAgreement;
    }

    public void setPurchaseAgreement(PurchaseAgreement purchaseAgreement) {
        this.purchaseAgreement = purchaseAgreement;
    }

    @Override
    public boolean hasConsecutiveDocuments() {
        DeliveryHistory deliveryHistory = DeliveryHistory.of(this);
        return !deliveryHistory.getDeliveryNotesNumbers().isEmpty()
                || !deliveryHistory.getInvoiceNumbers().isEmpty();
    }

}
