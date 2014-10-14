package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.report.ShippingCostsItem;

public class ShippingCostsItemBuilder extends ReportItemBuilder<ShippingCostsItem, Builder<ShippingCostsItem>> {

    Amount costs;

    DeliveryMethod deliveryMethod;

    @Override
    public ShippingCostsItem build() {
        ShippingCostsItem s = new ShippingCostsItem();
        s.setCosts(costs);
        s.setDeliveryMethod(deliveryMethod);
        return s;
    }

    public ShippingCostsItemBuilder setCosts(Amount amount) {
        this.costs = amount;
        return this;
    }

    public ShippingCostsItemBuilder setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
        return this;
    }

}
