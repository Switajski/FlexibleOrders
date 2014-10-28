package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;

@Entity
public class CatalogDeliveryMethod extends GenericEntity{

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
