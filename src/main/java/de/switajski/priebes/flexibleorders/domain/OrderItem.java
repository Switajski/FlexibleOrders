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

import de.switajski.priebes.flexibleorders.application.specification.CompletedSpecification;
import de.switajski.priebes.flexibleorders.application.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.application.specification.OrderedSpecification;
import de.switajski.priebes.flexibleorders.application.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Entity
@JsonAutoDetect
public class OrderItem extends GenericEntity implements Comparable<OrderItem> {

	@JsonIgnore
	@NotNull
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orderItem")
	private Set<ReportItem> deliveryHistory = new HashSet<ReportItem>();
	
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
	 * @param order
	 * @param product
	 * @param orderedQuantity
	 */
	public OrderItem(Order order, Product product, int orderedQuantity){
		this.deliveryHistory = new HashSet<ReportItem>();
		this.customerOrder = order;
		this.orderedQuantity = orderedQuantity;
		setProduct(product);
		
		// handle birectional relationship
		if (!order.getItems().contains(this))
			order.getItems().add(this);
	}
	
	public String toString(){
		String s = "#"+getId().toString() + ": " + getOrderedQuantity() + 
				" x " + getProduct().getName() + " " + provideStatus();
		return s;
	}
	
	@Override
	public int compareTo(OrderItem o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Set<ReportItem> getDeliveryHistory() {
		return deliveryHistory;
	}

	public void setDeliveryHistory(Set<ReportItem> deliveryHistory) {
		this.deliveryHistory = deliveryHistory;
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

	public Set<ReportItem> getAllHesOfType(ReportItemType type) {
		Set<ReportItem> hesOfType = new HashSet<ReportItem>();
		for (ReportItem he: deliveryHistory){
			if (he.getType() == type)
				hesOfType.add(he);
		}
		return hesOfType;
	}

	/**
	 * handles bidirectional relationship
	 * @param handlingEvent has no other Report than this. If null, this orderItem will be set.
	 */
	public void addHandlingEvent(ReportItem handlingEvent) {
		//prevent endless loop
		if (deliveryHistory.contains(handlingEvent)) return;
		deliveryHistory.add(handlingEvent);
		handlingEvent.setOrderItem(this);
	}
	
	public void removeHandlingEvent(ReportItem handlingEvent){
		if (!deliveryHistory.contains(handlingEvent)) return;
		deliveryHistory.remove(handlingEvent);
		handlingEvent.setOrderItem(null);
	}

	public Report getReport(String invoiceNo) {
		for (ReportItem he: getDeliveryHistory())
			if (he.getReport().getDocumentNumber().equals(invoiceNo))
				return he.getReport();
		return null;
	}
	
	/**
	 * returns the summed quantity of all HandlingEvents of given HandlingEventType
	 * 
	 * @return null if no HandlingEvents of given type found
	 */
	public Integer getHandledQuantity(ReportItemType type) {
		Set<ReportItem> hes = getAllHesOfType(type);
		if (hes.isEmpty()) return 0;
		int summed = 0;
		for (ReportItem he: hes){
			summed = summed + he.getQuantity();
		}
		return summed;
	}

	public String provideStatus() {
		String s = "";
		if (new CompletedSpecification().isSatisfiedBy(this)) s+= "fertig";
		else if (new ShippedSpecification(false, false).isSatisfiedBy(this)) s += "versendet";
		else if (new ConfirmedSpecification(false, false).isSatisfiedBy(this)) s+= "best&auml;tigt";
		else if (new OrderedSpecification().isSatisfiedBy(this)) s+= "bestellt";
		else s+= "abgebrochen";
		return s;
	}
	
	/**
	 * Creates a report item out of this order item.</br>
	 * </br>
	 * Report items in certain HandlingEvents are provided by 
	 * {@link OrderItem#toReportItems}
	 * 
	 * @return
	 */
	//TODO SRP: ReportItem should created by ReportItemMapper and in the Responsibility of 
	// handling event (a HandlingEventDTO instead of ReportItem)
	public ItemDto toItemDto(){
		ItemDto item = new ItemDto();
		item.setCreated(getCreated());
		item.setCustomer(getOrder().getCustomer().getId());
		item.setCustomerNumber(getOrder().getCustomer().getCustomerNumber());
		item.setCustomerName(getOrder().getCustomer().getLastName());
		item.setId(getId());
		if (getNegotiatedPriceNet() != null)
			item.setPriceNet(getNegotiatedPriceNet().getValue());
		item.setOrderNumber(getOrder().getOrderNumber());
		item.setProduct(getProduct().getProductNumber());
		item.setProductName(getProduct().getName());
		item.setStatus(provideStatus());
		item.setQuantity(getOrderedQuantity());
		item.setQuantityLeft(getOrderedQuantity() - getHandledQuantity(ReportItemType.CONFIRM));
		return item;
	}
	
	/**
	 * Creates report items of this order item. </br>
	 * 
	 * @param type
	 * @return
	 */
	//TODO SRP: ReportItem should be a mapping matter (e.g. DTO) matter using a framework like dozer
	public Set<ItemDto> toReportItems(ReportItemType type){
		Set<ItemDto> ris = new HashSet<ItemDto>();
		if (type != null){
			for (ReportItem he: this.getAllHesOfType(type)){
				ItemDto item = he.toItemDto();
				item.setQuantityLeft(calculateQuantityLeft(type));
				ris.add(item);
			}
		} else 
			ris.add(this.toItemDto());
		return ris;
	}

	/**
	 * Provides the quantity stuck in given {@link ReportItemType}.
	 * @param type HandlingEventType representing a state
	 * @return quantity left in given {@link ReportItemType}
	 * @see ItemDto#getQuantityLeft
	 */
	//TODO: srp!!! move to Handling Event or make a factory
	public int calculateQuantityLeft(ReportItemType type) {
		int quantityLeft = 0;
		switch (type) {
		case CONFIRM:
			quantityLeft = getOrderedQuantity() - getHandledQuantity(ReportItemType.SHIP);
			break;
		case SHIP:
			quantityLeft = getHandledQuantity(ReportItemType.SHIP) 
					- getHandledQuantity(ReportItemType.INVOICE);
			break;
		case INVOICE:
			quantityLeft = getHandledQuantity(ReportItemType.INVOICE) 
					- getHandledQuantity(ReportItemType.PAID);
			break;
		case PAID:
			quantityLeft = getHandledQuantity(ReportItemType.PAID);
//					- getHandledQuantity(HandlingEventType.INVOICE);
			break;
		case CANCEL:
			break;
		case FORWARD_TO_THIRD_PARTY:
			break;
		default:
			break;
		}
		return quantityLeft;
	}

	public boolean isShippingCosts() {
		return this.getProduct().getProductType().equals(ProductType.SHIPPING);
	}
	
}
