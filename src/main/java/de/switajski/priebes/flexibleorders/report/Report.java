package de.switajski.priebes.flexibleorders.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.reference.Status;

public class Report<T extends Item> {
	private Customer customer;

	private Long orderNumber;

	private HashMap<Status, List<T>> items = new HashMap<Status, List<T>>();

	private Date created;
	
	private double taxRate = 0.19;
	
	public Report(List<T> items) {
		setItems(items);
	}

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

	public List<T> getItems() {
		ArrayList<T> ois = new ArrayList<T>();
		for (Status s:Status.values())
			for (T oi:items.get(s)){
				ois.add(oi);
			}
		return ois;
	}

	/**
	 * Set the items, on which a report is based upon
	 * @param items
	 */
	public void setItems(List<T> items) {
		if (items==null) throw new IllegalArgumentException("Items for report must not be null");
		
		Item item = items.get(0);
		setCustomer(item.getCustomer());
		setOrderNumber(item.getOrderNumber());
		setCreated(item.getCreated());
		
		setItemsToHashMap(items);
	}
	
	
	private void setItemsToHashMap(List<T> items2) {
		for (Status s:Status.values())
			this.items.put(s, new ArrayList<T>());
		for (T oi:items2){
			this.items.get(oi.getStatus()).add(oi);
		}
		
	}

	public List<T> getItems(Status status){
		return items.get(status);
	}

	public Status getStatus() {
		int size = countItems(); 
		for (Status status:Status.values())
			if (size == getItems(status).size())
				return status;
		return null;
	}
	
	public int countItems(){
		return getItems().size();
	}

	public Date getCreated() {
		return created;
	}

	private void setCreated(Date created) {
		this.created = created;
	}

	public BigDecimal getNetAmount() {
		BigDecimal netAmount = new BigDecimal(0);
		for (Item item:getItems()){
			BigDecimal amount = 
					item.getPriceNet()
					.multiply(new BigDecimal(item.getQuantity())); 
			netAmount.add(amount);
		}
		return netAmount;
	}

	public BigDecimal getGrossAmount() {
		return getNetAmount().add(getTax());
	}
	
	public BigDecimal getTax(){
		BigDecimal productSumNet = BigDecimal.ZERO;
		for (Item i:getItems())
			if (i.getProduct().getProductType().equals(ProductType.PRODUCT))
				productSumNet.add(
						i.getPriceNet()
						.multiply(
								new BigDecimal(i.getQuantity()))
								);
		return productSumNet.multiply(new BigDecimal(getTaxRate()));
	}

	public double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(double tax) {
		this.taxRate = tax;
	}

	
}
