package de.switajski.priebes.flexibleorders.domain.report;

import java.util.Date;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Entity
public class ConfirmationItem extends ReportItem {

    private Boolean pending = false;

    public Boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public ConfirmationItem() {}

    /**
     * Convenience method
     */
    @JsonIgnore
    public OrderConfirmation getOrderConfirmation() {
        return (OrderConfirmation) report;
    }

    public ConfirmationItem(OrderConfirmation orderConfirmation, OrderItem oi, Integer quantityLeft) {
        super(oi, quantityLeft, new Date());
        if (orderConfirmation != null) orderConfirmation.addItem(this);
    }

    @Override
    public String provideStatus() {
        return "best&auml;tigt";
    }

    /**
     * convenience method
     * 
     * @return
     */
    public boolean isAgreed() {
        return getOrderConfirmation().isAgreed();
    }

}
