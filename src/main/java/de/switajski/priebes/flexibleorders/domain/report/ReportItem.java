package de.switajski.priebes.flexibleorders.domain.report;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.GenericEntity;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class ReportItem extends GenericEntity implements
        Comparable<ReportItem> {

    @NotNull
    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    protected Report report;

    @NotNull
    @ManyToOne
    private OrderItem orderItem;

    protected ReportItem() {}

    /**
     * @deprecated use {@link #ReportItem(OrderItem, Integer, Date)} instead.
     *             The API is meant to create a ConfirmationItem first and then
     *             add to a ConfirmationReport
     */
    public ReportItem(Report report, OrderItem item,
            Integer quantity, Date created) {
        if (item == null || quantity == null) throw new IllegalArgumentException();
        this.report = report;
        this.orderItem = item;
        this.quantity = quantity;
        setCreated(created);

        if (!orderItem.getReportItems().contains(this)) orderItem.addReportItem(this);
        if (report.getItems().contains(this)) return;
        report.addItem(this);
    }

    public ReportItem(OrderItem item, Integer quantity, Date created) {
        if (item == null || quantity == null) {
            throw new IllegalArgumentException();
        }
        this.orderItem = item;
        this.quantity = quantity;
        setCreated(created);

        if (!orderItem.getReportItems().contains(this)) orderItem.addReportItem(this);
        if (report != null) {
            if (report.getItems().contains(this)) return;
            report.addItem(this);
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
        if (report.getItems().contains(this)) return;
        report.addItem(this);
    }

    @JsonIgnore
    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem item) {
        this.orderItem = item;

        // handle bidirectional relationship:
        if (item.getReportItems().contains(this)) return;
        item.addReportItem(this);
    }

    @Override
    public int compareTo(ReportItem o) {
        // TODO Auto-generated method stub
        return 0;
    }

    public abstract String provideStatus();

    @Override
    public String toString() {
        return new StringBuilder(this.getClass().getSimpleName())
                .append(": ").append(" " + getQuantity()).toString();
    }

    /**
     * convenience method
     * 
     * @return
     */
    @JsonIgnore
    public Customer getCustomer() {
        return this.getOrderItem().getCustomer();
    }

    public DeliveryHistory createDeliveryHistory() {
        return DeliveryHistory.of(this);
    }

}
