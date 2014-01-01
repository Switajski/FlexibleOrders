package de.switajski.priebes.flexibleorders.domain.specification;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;

public class ConfirmedSpecification extends ItemSpecification{

	@NotNull
	private long orderConfirmationNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date expectedDelivery;

	private Amount negotiatedPriceNet;
	
	private Address invoiceAddress;

//	TODO: find a way to embed two different addresses
//	private Address shippingAddress;

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getExpectedDelivery() {
		return expectedDelivery;
	}
	
	public ConfirmedSpecification() {}
	
	/**
	 * Constructor with attributes needed for a valid order confirmation 
	 * 
	 * @param orderConfirmationNumber
	 * @param expectedDelivery
	 * @param negotiatedPriceNet
	 * @param invoiceAddress
	 */
	public ConfirmedSpecification(long orderConfirmationNumber, 
			Date expectedDelivery, 
			Amount negotiatedPriceNet, 
			Address invoiceAddress) {
		this.orderConfirmationNumber = orderConfirmationNumber;
		this.expectedDelivery = expectedDelivery;
		this.negotiatedPriceNet = negotiatedPriceNet;
		this.invoiceAddress = invoiceAddress;
	}
	
	public boolean isSatisfiedBy(Item item){
		for (HandlingEvent he: item.getDeliveryHistory().getAllHesOfType(HandlingEventType.ORDERCONFIRM)){
			if (he.getConfirmedSpec().getInvoiceAddress() == null) return false;
			if (he.getConfirmedSpec().getNegotiatedPriceNet() == null) return false;
			if (he.getConfirmedSpec().getOrderConfirmationNumber() < 0) return false;
		}
		return true;
	}

    @JsonDeserialize(using = JsonDateDeserializer.class)
	public void setExpectedDelivery(Date expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
	}

	public long getOrderConfirmationNumber() {
		return orderConfirmationNumber;
	}

	public Amount getNegotiatedPriceNet() {
		return negotiatedPriceNet;
	}

	public Address getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(Address invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	@Override
	public Predicate toPredicate(Root<Item> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		// TODO Auto-generated method stub
		return null;
	}

//	public Address getShippingAddress() {
//		return shippingAddress;
//	}
//
//	public void setShippingAddress(Address shippingAddress) {
//		this.shippingAddress = shippingAddress;
//	}

}
