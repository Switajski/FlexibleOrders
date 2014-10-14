package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import org.joda.time.LocalDate;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;

public class PurchaseAgreementBuilder implements Builder<PurchaseAgreement> {

    private Address invoiceAddress;

    private Address shippingAddress;

    private DeliveryMethod deliveryMethod;

    private LocalDate expectedDelivery;

    private Long customerNumber;
    
    @Override
    public PurchaseAgreement build() {
        PurchaseAgreement p = new PurchaseAgreement();
        p.setInvoiceAddress(invoiceAddress);
        p.setShippingAddress(shippingAddress);
        p.setDeliveryMethod(deliveryMethod);
        p.setExpectedDelivery(expectedDelivery);
        p.setCustomerNumber(customerNumber);
        return p;
    }

    public PurchaseAgreementBuilder setInvoiceAddress(Address invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
        return this;
    }

    public PurchaseAgreementBuilder setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public PurchaseAgreementBuilder setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
        return this;
    }

    public PurchaseAgreementBuilder setExpectedDelivery(LocalDate expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
        return this;
    }

    public PurchaseAgreementBuilder setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
        return this;
    }

}
