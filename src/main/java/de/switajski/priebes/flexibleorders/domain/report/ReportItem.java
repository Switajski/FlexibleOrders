package de.switajski.priebes.flexibleorders.domain.report;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.GenericEntity;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.service.FOStringUtility;
import de.switajski.priebes.flexibleorders.service.QuantityUtility;

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
        return new StringBuilder("#" + getId() + " " + this.getClass().getSimpleName())
                .append(": ").append(" ").append(getQuantity()).append(" ").append(getOrderItem()).toString();
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

    public int toBeProcessed() {
        DeliveryHistory history = DeliveryHistory.of(this);
        if (this instanceof ConfirmationItem) {
            if (!((ConfirmationItem) this).isAgreed()) {
                return toBeAgreed(history);
            }
            else {
                return toBeShipped(history);
            }
        }
        else if (this instanceof ShippingItem) return toBeInvoiced(history);
        else if (this instanceof InvoiceItem) return toBePaid(history);
        else return 0;
    }

    public Integer toBeAgreed(DeliveryHistory history) {
        return QuantityUtility.sumQty(history.getNonAgreedConfirmationItems()) - QuantityUtility.sumQty(history.getAgreedConfirmationItems());
    }

    public int toBeShipped(DeliveryHistory history) {
        return QuantityUtility.sumQty(history.getAgreedConfirmationItems()) - QuantityUtility.sumQty(history.getReportItems(ShippingItem.class));
    }

    public int toBeInvoiced(DeliveryHistory history) {
        return QuantityUtility.sumQty(history.getReportItems(ShippingItem.class)) - QuantityUtility.sumQty(history.getReportItems(InvoiceItem.class));
    }

    public int toBePaid(DeliveryHistory history) {
        return QuantityUtility.sumQty(history.getReportItems(InvoiceItem.class)) - QuantityUtility.sumQty(history.getReportItems(ReceiptItem.class));
    }

    @PrePersist
    protected void validateQuantity() {
        if (quantity < 1) {
            throw new IllegalStateException("Quantity must be at least 1");
        }
        if (sum() > getOrderItem().getOrderedQuantity()) {
            throw new IllegalStateException(
                    new StringBuilder().append("Sum of all ")
                            .append(FOStringUtility.camelCaseToSplitted(this.getClass().getSimpleName()))
                            .append("(s) cannot be greater than ordered quantity of ")
                            .append(FOStringUtility.camelCaseToSplitted(OrderItem.class.getSimpleName()))
                            .toString());
        }
    }

    private int sum() {
        int sum = 0;
        for (ReportItem specificReportItem : getOrderItem().getReportItems(this.getClass())) {
            sum += specificReportItem.getQuantity();
        }
        return sum;
    }

}
