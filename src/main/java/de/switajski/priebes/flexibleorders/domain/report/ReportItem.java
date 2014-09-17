package de.switajski.priebes.flexibleorders.domain.report;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.switajski.priebes.flexibleorders.application.QuantityCalculator;
import de.switajski.priebes.flexibleorders.domain.GenericEntity;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

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

    // TODO: refactor to no constructor - use factory instead
    public ReportItem(Report report, OrderItem item,
            Integer quantity, Date created) {
        if (report == null || item == null || quantity == null) throw new IllegalArgumentException();
        this.report = report;
        this.orderItem = item;
        this.quantity = quantity;
        setCreated(created);

        if (!orderItem.getReportItems().contains(this)) orderItem.addReportItem(this);
        if (report.getItems().contains(this)) return;
        report.addItem(this);
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
        if (report.getItems().contains(this)) return;
        this.report = report;
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

    /**
     * Not quantity left is set!
     * 
     * @return
     * @see OrderItem#toReportItems(ReportItemType)
     */
    @Deprecated
    public ItemDto toItemDto() {
        // TODO: enhance mapping by a mapping framework
        ItemDto item = new ItemDto();
        item.documentNumber = this.getReport().getDocumentNumber();
        // TODO: instanceof: this is not subject of this class
        if (this.getReport() instanceof OrderConfirmation) {
            item.orderConfirmationNumber = this.getReport().getDocumentNumber();
            // TODO: DRY
            PurchaseAgreement pa = ((OrderConfirmation) this.getReport()).getPurchaseAgreement();
            if (pa != null) item.expectedDelivery = pa.getExpectedDelivery();
        }
        if (this.getReport() instanceof OrderAgreement) {
            item.orderAgreementNumber = this.getReport().getDocumentNumber();
            // TODO: DRY
            PurchaseAgreement pa = ((OrderAgreement) this.getReport()).getAgreementDetails();
            if (pa != null) item.expectedDelivery = pa.getExpectedDelivery();
        }
        if (this.getReport() instanceof Invoice) {
            Invoice invoice = (Invoice) this.getReport();
            item.invoiceNumber = this.getReport().getDocumentNumber();
            item.deliveryNotesNumber = this.getReport().getDocumentNumber();
            item.paymentConditions = invoice.getPaymentConditions();
        }
        if (this.getReport() instanceof DeliveryNotes) {
            DeliveryNotes deliveryNotes = (DeliveryNotes) this.getReport();
            item.deliveryNotesNumber = this.getReport().getDocumentNumber();
            item.trackNumber = deliveryNotes.getTrackNumber();
            item.packageNumber = deliveryNotes.getPackageNumber();
        }
        if (this.getReport() instanceof Receipt) {
            item.receiptNumber = this.getReport().getDocumentNumber();
        }
        item.created = this.getCreated();
        Order order = this.getOrderItem().getOrder();
        item.customer = order.getCustomer().getId();
        item.customerNumber = order.getCustomer().getCustomerNumber();
        item.customerName = order.getCustomer().getLastName();
        item.documentNumber = this.getReport().getDocumentNumber();
        item.id = this.getId();
        item.orderNumber = order.getOrderNumber();
        if (this.getOrderItem().getNegotiatedPriceNet() != null) item.priceNet = this.getOrderItem().getNegotiatedPriceNet().getValue();
        item.product = this.getOrderItem().getProduct().getProductNumber();
        item.productName = this.getOrderItem().getProduct().getName();
        item.quantity = this.getQuantity();
        item.status = this.provideStatus();
        item.quantityLeft = QuantityCalculator.calculateLeft(this);
        return item;
    }

    public abstract String provideStatus();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + this.getReport().getDocumentNumber() + " " + getQuantity() + " x "
                + getOrderItem().getProduct().getName();
    }

}
