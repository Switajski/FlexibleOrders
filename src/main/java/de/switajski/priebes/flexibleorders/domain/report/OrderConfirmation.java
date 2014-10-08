package de.switajski.priebes.flexibleorders.domain.report;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;

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
        return getOrderAgreementNumber() != null;
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

}
