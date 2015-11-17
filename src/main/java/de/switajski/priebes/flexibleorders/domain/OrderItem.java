package de.switajski.priebes.flexibleorders.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.CancellationItem;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.service.QuantityUtility;

@Entity
@JsonAutoDetect
public class OrderItem extends GenericEntity implements Comparable<OrderItem> {

    @JsonIgnore
    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderItem")
    private Set<ReportItem> reportItems = new HashSet<ReportItem>();

    @NotNull
    private Integer orderedQuantity;

    private Amount negotiatedPriceNet;

    @NotNull
    @Embedded
    private Product product;

    private String packageNumber;

    private String trackingNumber;

    @JsonIgnore
    @NotNull
    @ManyToOne()
    private Order customerOrder;

    protected OrderItem() {}

    /**
     * Constructor with all attributes needed to create a valid item
     * 
     * @param order
     * @param product
     * @param orderedQuantity
     */
    public OrderItem(Order order, Product product, int orderedQuantity) {
        this.reportItems = new HashSet<ReportItem>();
        this.customerOrder = order;
        this.orderedQuantity = orderedQuantity;
        setProduct(product);

        // handle birectional relationship
        if (order != null && !order.getItems().contains(this)) order.getItems().add(this);
    }

    public String toString() {
        return new StringBuilder("#")
                .append(getId())
                .append(": ")
                .append(getOrderedQuantity())
                .append(" x ")
                .append(getProduct().getName())
                .toString();
    }
    
    public int toBeConfirmed() {
        DeliveryHistory deliveryHistory = DeliveryHistory.of(this);
        if (deliveryHistory.isEmpty()) {
            return orderedQuantity;
        }
        return orderedQuantity - QuantityUtility.sumQty(deliveryHistory.getNonAgreedConfirmationItems());
    }

    @Override
    public int compareTo(OrderItem o) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Set<ReportItem> getReportItems() {
        return reportItems;
    }

    public void setReportItems(Set<ReportItem> reportItems) {
        this.reportItems = reportItems;
    }

    public Amount getNegotiatedPriceNet() {
        return negotiatedPriceNet;
    }

    public void setNegotiatedPriceNet(Amount negotiatedPriceNet) {
        this.negotiatedPriceNet = negotiatedPriceNet;
    }

    public Integer getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(Integer orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public Order getOrder() {
        return customerOrder;
    }

    public void setOrder(Order order) {
        if (!order.getItems().contains(this)) order.getItems().add(this);
        this.customerOrder = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(String packageNumber) {
        this.packageNumber = packageNumber;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    /**
     * handles bidirectional relationship
     * 
     * @param reportItem
     *            has no other Report than this. If null, this orderItem will be
     *            set.
     */
    public void addReportItem(ReportItem reportItem) {
        // prevent endless loop
        if (reportItems.contains(reportItem)) return;
        reportItems.add(reportItem);
        reportItem.setOrderItem(this);
    }

    public void removeHandlingEvent(ReportItem handlingEvent) {
        if (!reportItems.contains(handlingEvent)) return;
        reportItems.remove(handlingEvent);
        handlingEvent.setOrderItem(null);
    }

    public Report getReport(String invoiceNo) {
        for (ReportItem he : getReportItems())
            if (he.getReport().getDocumentNumber().equals(invoiceNo)) return he.getReport();
        return null;
    }

    public Set<ConfirmationItem> getConfirmationItems() {
        return getReportItems(ConfirmationItem.class);
    }

    public Set<ShippingItem> getShippingItems() {
        return getReportItems(ShippingItem.class);
    }

    @SuppressWarnings("unchecked")
	public <T> Set<T> getReportItems(Class<T> type){
    	Set<T> riToReturn = new HashSet<>();
    	for (ReportItem item:reportItems){
    		if (type.isInstance(item)){
    			riToReturn.add((T) item);
    		}
    	}
    	return riToReturn;
    }

    public boolean isShippingCosts() {
        return this.getProduct().getProductType().equals(ProductType.SHIPPING);
    }

    /**
     * convenience method
     * 
     * @return
     */
    @JsonIgnore
    public Customer getCustomer() {
        return this.getOrder().getCustomer();
    }

}
