package de.switajski.priebes.flexibleorders.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Invoice extends Report {

	private String paymentConditions;
	
	@NotNull
	private Address invoiceAddress;
	
	protected Invoice() {}
	
	public Invoice(String invoiceNumber, String paymentConditions, Address invoiceAddress) {
		super(invoiceNumber);
		this.paymentConditions = paymentConditions;
		this.invoiceAddress = invoiceAddress;
	}
	
	
	public String getPaymentConditions() {
		return paymentConditions;
	}

	public void setPaymentConditions(String paymentConditions) {
		this.paymentConditions = paymentConditions;
	}


	public Address getInvoiceAddress() {
		return invoiceAddress;
	}


	public void setInvoiceAddress(Address invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public Map<String, Amount> getShippingCosts() {
		Map<String, Amount> shippingCosts = new HashMap<String, Amount>();
		for (HandlingEvent he : this.getEvents()){
			OrderItem orderItem = he.getOrderItem();
			if (orderItem.isShippingCosts())
				shippingCosts.put(getDeliveryNotesNoOfShippingCosts(orderItem), orderItem.getNegotiatedPriceNet());
		}
		return shippingCosts;
	}
	
	public String getDeliveryNotesNoOfShippingCosts(OrderItem shippingCostsItem){
		return shippingCostsItem.getAllHesOfType(HandlingEventType.SHIP).iterator().next().getReport().getDocumentNumber();
	}

}
