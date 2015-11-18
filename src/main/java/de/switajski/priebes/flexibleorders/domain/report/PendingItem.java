package de.switajski.priebes.flexibleorders.domain.report;

import java.util.Date;

import javax.persistence.Entity;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Entity
public class PendingItem extends ReportItem {

    public PendingItem() {}

    public PendingItem(DeliveryNotes deliveryNotes, OrderItem orderItem, Integer quantityToDeliver, Date date) {
        super(orderItem, quantityToDeliver, date);
        report = deliveryNotes;
    }

    @Override
    public String provideStatus() {
        return "ausstehend";
    }

}
