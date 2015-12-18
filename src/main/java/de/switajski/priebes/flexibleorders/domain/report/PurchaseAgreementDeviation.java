package de.switajski.priebes.flexibleorders.domain.report;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import de.switajski.priebes.flexibleorders.domain.GenericEntity;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;

@Entity
public class PurchaseAgreementDeviation extends GenericEntity {

    @NotNull
    @ManyToOne
    private OrderConfirmation orderConfirmation;

    @Embedded
    private PurchaseAgreement purchaseAgreement;

    public PurchaseAgreement getPurchaseAgreement() {
        return purchaseAgreement;
    }

    public void setPurchaseAgreement(PurchaseAgreement purchaseAgreement) {
        this.purchaseAgreement = purchaseAgreement;
    }

    public OrderConfirmation getOrderConfirmation() {
        return orderConfirmation;
    }

    public void setOrderConfirmation(OrderConfirmation orderConfirmation) {
        this.orderConfirmation = orderConfirmation;
    }
}
