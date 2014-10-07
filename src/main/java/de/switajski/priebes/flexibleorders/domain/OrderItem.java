package de.switajski.priebes.flexibleorders.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;

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

	protected OrderItem() {
	}

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
		if (order != null && !order.getItems().contains(this))
			order.getItems().add(this);
	}

	public String toString() {
		String s = "#" + getId().toString() + ": " + getOrderedQuantity() +
				" x " + getProduct().getName() + " " + DeliveryHistory.of(this).provideStatus();
		return s;
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
		if (!order.getItems().contains(this))
			order.getItems().add(this);
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
		if (reportItems.contains(reportItem))
			return;
		reportItems.add(reportItem);
		reportItem.setOrderItem(this);
	}

	public void removeHandlingEvent(ReportItem handlingEvent) {
		if (!reportItems.contains(handlingEvent))
			return;
		reportItems.remove(handlingEvent);
		handlingEvent.setOrderItem(null);
	}
	
	public Report getReport(String invoiceNo) {
		for (ReportItem he : getReportItems())
			if (he.getReport().getDocumentNumber().equals(invoiceNo))
				return he.getReport();
		return null;
	}

	public Set<ConfirmationItem> getConfirmationItems() {
		Set<ConfirmationItem> riToReturn = new HashSet<ConfirmationItem>();
		for (ReportItem ri : getReportItems()) {
			if (ri instanceof ConfirmationItem)
				riToReturn.add((ConfirmationItem) ri);
		}
		return riToReturn;
	}

	public Set<InvoiceItem> getInvoiceItems() {
		Set<InvoiceItem> riToReturn = new HashSet<InvoiceItem>();
		for (ReportItem ri : getReportItems()) {
			if (ri instanceof InvoiceItem)
				riToReturn.add((InvoiceItem) ri);
		}
		return riToReturn;
	}

	public Set<ReceiptItem> getReceiptItems() {
		Set<ReceiptItem> riToReturn = new HashSet<ReceiptItem>();
		for (ReportItem ri : getReportItems()) {
			if (ri instanceof ReceiptItem)
				riToReturn.add((ReceiptItem) ri);
		}
		return riToReturn;
	}

	public Set<ShippingItem> getShippingItems() {
		Set<ShippingItem> riToReturn = new HashSet<ShippingItem>();
		for (ReportItem ri : getReportItems()) {
			if (ri instanceof ShippingItem)
				riToReturn.add((ShippingItem) ri);
		}
		return riToReturn;
	}

	public Set<CancellationItem> getCancellationItems() {
		Set<CancellationItem> riToReturn = new HashSet<CancellationItem>();
		for (ReportItem ri : getReportItems()) {
			if (ri instanceof CancellationItem)
				riToReturn.add((CancellationItem) ri);
		}
		return riToReturn;
	}

	public boolean isShippingCosts() {
		return this.getProduct().getProductType().equals(ProductType.SHIPPING);
	}

	/**
	 * convenience method
	 * @return
	 */
	@JsonIgnore
	public Customer getCustomer() {
		return this.getOrder().getCustomer();
	}

}
