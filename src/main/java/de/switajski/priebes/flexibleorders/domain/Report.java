package de.switajski.priebes.flexibleorders.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.json.CustomerToIdSerializer;
import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.reference.Status;

@JsonAutoDetect
public class Report {
	
	private Customer customer;

	private Long orderNumber;
	
	private Long orderConfirmationNumber;
	
	private Long invoiceNumber;
	
	private Long accountNumber;

	private HashMap<Status, List<Item>> items = new HashMap<Status, List<Item>>();

	private double taxRate = 0.19;
	
	private Date created;
	
	public Report(List<Item> items) {
		Item item = items.get(0);
		if (items.isEmpty()) throw new IllegalArgumentException("items are empty!");
		setItems(items);
		this.orderNumber = item.getOrderNumber();
	}

	@JsonSerialize(using=CustomerToIdSerializer.class)
	public Customer getCustomer() {
		return customer;
	}

	private void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	private void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<Item> getItems() {
		ArrayList<Item> ois = new ArrayList<Item>();
		for (Status s:Status.values())
			for (Item oi:items.get(s)){
				ois.add(oi);
			}
		return ois;
	}

	/**
	 * Set the items, on which a report is based upon
	 * @param items
	 */
	public void setItems(List<Item> items) {
		if (items==null) throw new IllegalArgumentException("Items for report must not be null");
		
		Item item = items.get(0);
		setCustomer(item.getCustomer());
		setOrderNumber(item.getOrderNumber());
		setCreated(item.getCreated());
		
	}
	
	
	public List<Item> getItems(Status status){
		return items.get(status);
	}

	public Status getStatus() {
		int size = size(); 
		for (Status status:Status.values())
			if (size == getItems(status).size())
				return status;
		return null;
	}
	
	public int size(){
		return getItems().size();
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	private void setCreated(Date created) {
		this.created = created;
	}

	public BigDecimal getNetAmount() {
		throw new NotImplementedException();
//		BigDecimal netAmount = new BigDecimal(0);
//		for (Item item:getItems()){
//			//FIXME: Multiply only same Currency
//			BigDecimal amount = 
//					item.getConfirmedSpec().getNegotiatedPriceNet().getValue()
//					.multiply(new BigDecimal(item.getQuantity())); 
//			netAmount = netAmount.add(amount);
//		}
//		return netAmount;
	}

	public BigDecimal getGrossAmount() {
		return getNetAmount().add(getTax());
	}
	
	public BigDecimal getTax(){
		throw new NotImplementedException();
//		BigDecimal productSumNet = BigDecimal.ZERO;
//		for (Item i:getItems())
//				productSumNet = productSumNet.add(
//						//FIXME: Multiply only same Currency
//						i.getConfirmedSpec().getNegotiatedPriceNet().getValue()
//						.multiply(
//								new BigDecimal(i.getQuantity()))
//								);
//		return productSumNet.multiply(new BigDecimal(getTaxRate()));
	}

	public double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(double tax) {
		this.taxRate = tax;
	}
	
	public Long getOrderConfirmationNumber() {
		return orderConfirmationNumber;
	}

	public void setOrderConfirmationNumber(Long orderConfirmationNumber) {
		this.orderConfirmationNumber = orderConfirmationNumber;
	}

	public Long getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long archiveNumber) {
		this.accountNumber = archiveNumber;
	}

	public Date getCreated() {
		return created;
	}

	public void setItems(HashMap<Status, List<Item>> items) {
		this.items = items;
	}

	
}
