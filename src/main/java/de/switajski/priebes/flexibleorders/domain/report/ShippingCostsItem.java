package de.switajski.priebes.flexibleorders.domain.report;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;

@Entity
public class ShippingCostsItem extends ReportItem {

    @Embedded
    private Amount costs;
    
    @Embedded
    private DeliveryMethod deliveryMethod;

    public Amount getCosts() {
        return costs;
    }

    public void setCosts(Amount costs) {
        this.costs = costs;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    @Override
    public String provideStatus() {
        return "versendet";
    }

}
