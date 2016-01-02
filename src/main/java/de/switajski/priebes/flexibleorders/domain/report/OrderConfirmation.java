package de.switajski.priebes.flexibleorders.domain.report;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderConfirmation")
    private List<PurchaseAgreementDeviation> deviations;

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

    /**
     * Latest first
     * 
     * @return
     */
    public List<PurchaseAgreementDeviation> getDeviations() {
        if (deviations == null) return Collections.emptyList();
        return deviations.stream()
                .sorted((pa1, pa2) -> pa2.getCreated().compareTo(pa1.getCreated()))
                .collect(Collectors.toList());
    }

    public void setDeviations(List<PurchaseAgreementDeviation> deviations) {
        this.deviations = deviations;
    }

    public void addDeviation(PurchaseAgreement pa) {
        PurchaseAgreementDeviation pad = new PurchaseAgreementDeviation();
        pad.setOrderConfirmation(this);
        pad.setPurchaseAgreement(pa);
        deviations.add(pad);
    }

    public PurchaseAgreement actualPurchaseAgreement() {
        return getDeviations().stream()
                .map(pa -> pa.getPurchaseAgreement())
                .findFirst()
                .orElse(purchaseAgreement);
    }

    public void changeShippingAddress(Address shippingAddress) {
        PurchaseAgreement pa = new PurchaseAgreement(actualPurchaseAgreement());
        pa.setShippingAddress(shippingAddress);
        addDeviation(pa);
    }

}
