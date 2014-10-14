package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Embedded;

import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;

public class CatalogDeliveryMethod {

    @Embedded
    private DeliveryMethod deliveryMethod;

    public CatalogDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public CatalogDeliveryMethod() {
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }
}
