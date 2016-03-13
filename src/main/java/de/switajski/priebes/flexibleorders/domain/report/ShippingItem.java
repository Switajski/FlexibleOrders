package de.switajski.priebes.flexibleorders.domain.report;

import java.util.Date;

import javax.persistence.Entity;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Entity
public class ShippingItem extends ReportItem {

    private String packageNumber, trackNumber;

    public ShippingItem() {}

    public ShippingItem(
            DeliveryNotes deliveryNotes,
            OrderItem orderItemToBeDelivered,
            Integer quantityToDeliver,
            Date date) {
        super(
                orderItemToBeDelivered,
                quantityToDeliver,
                date);
        if (report != null) report.addItem(this);
    }

    @Override
    public String provideStatus() {
        return "geliefert";
    }

    public DeliveryNotes getDeliveryNotes() {
        return (DeliveryNotes) this.report;
    }

    public String getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(String packageNumber) {
        this.packageNumber = packageNumber;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

}
