package de.switajski.priebes.flexibleorders.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.CancellationItem;
import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

//TODO use on read only objects
public class DeliveryHistory {

	private Set<ReportItem> reportItems;
	
	public DeliveryHistory(Set<ReportItem> reportItems) {
		this.reportItems = reportItems;
	}

	public Set<ConfirmationItem> getConfirmationItems() {
		Set<ConfirmationItem> riToReturn = new HashSet<ConfirmationItem>();
		for (ReportItem ri : reportItems) {
			if (ri instanceof ConfirmationItem)
				riToReturn.add((ConfirmationItem) ri);
		}
		return riToReturn;
	}
	
	public Set<InvoiceItem> getInvoiceItems() {
		Set<InvoiceItem> riToReturn = new HashSet<InvoiceItem>();
		for (ReportItem ri : reportItems) {
			if (ri instanceof InvoiceItem)
				riToReturn.add((InvoiceItem) ri);
		}
		return riToReturn;
	}

	public Set<ReceiptItem> getReceiptItems() {
		Set<ReceiptItem> riToReturn = new HashSet<ReceiptItem>();
		for (ReportItem ri : reportItems) {
			if (ri instanceof ReceiptItem)
				riToReturn.add((ReceiptItem) ri);
		}
		return riToReturn;
	}

	public Set<ShippingItem> getShippingItems() {
		Set<ShippingItem> riToReturn = new HashSet<ShippingItem>();
		for (ReportItem ri : reportItems) {
			if (ri instanceof ShippingItem)
				riToReturn.add((ShippingItem) ri);
		}
		return riToReturn;
	}

	public Set<ReportItem> getItems() {
		return reportItems;
	}

	public List<ReportItem> getItemsSorted() {
		List<ReportItem> ris = new ArrayList<ReportItem>(getItems());
		Collections.sort(ris, new Comparator<ReportItem>() {
			@Override
			public int compare(ReportItem o1, ReportItem o2) {
				return o1.getCreated().compareTo(o2.getCreated());
			}
		});
		return ris;
	}

	public boolean isEmpty() {
		return reportItems.isEmpty();
	}

	public Set<CancellationItem> getCancellationItems() {
		Set<CancellationItem> riToReturn = new HashSet<CancellationItem>();
		for (ReportItem ri : reportItems) {
			if (ri instanceof CancellationItem)
				riToReturn.add((CancellationItem) ri);
		}
		return riToReturn;
	}

	public String getOrderNumber() {
		return this.reportItems
				.iterator()
				.next()
				.getOrderItem()
				.getOrder()
				.getOrderNumber();
	}

	public String toString() {
		String s = "";
		OrderItem orderItem = this.reportItems.iterator().next().getOrderItem();
		s += "Bnr: " + orderItem.getOrder().getOrderNumber();
		s += ", " + orderItem.getOrderedQuantity() + " x ";
		s += +orderItem.getProduct().getProductNumber() + " "
				+ orderItem.getProduct().getName();
		return s;
	}
	
	public ShippingItem getShippingItemOf(InvoiceItem ii){
		Set<ShippingItem> sis = this.getShippingItems();
		if (sis.size() > 1)
			throw new IllegalStateException("Mehr als eine zutreffende Lieferscheinposition gefunden");
		else return sis.iterator().next();
	}

	public Address getShippingAddress() {
		ConfirmationReportAttributeRetriever<Address> retr = new ConfirmationReportAttributeRetriever<Address>(this) {

			@Override
			public Address retrieveAttribute(ConfirmationItem si) {
				return ((ConfirmationReport) si.getReport()).getShippingAddress();
			}
		};
		
		return retr.getInvoiceAddress();
	}

	public Address getInvoiceAddress(){
		ConfirmationReportAttributeRetriever<Address> retr = new ConfirmationReportAttributeRetriever<Address>(this) {

			@Override
			public Address retrieveAttribute(ConfirmationItem si) {
				return ((ConfirmationReport) si.getReport()).getInvoiceAddress();
			}
		};
		
		return retr.getInvoiceAddress();
	}
	
	public CustomerDetails getCustomerDetails(){
		ConfirmationReportAttributeRetriever<CustomerDetails> attr = new ConfirmationReportAttributeRetriever<CustomerDetails>(this) {

			@Override
			public CustomerDetails retrieveAttribute(ConfirmationItem si) {
				return ((ConfirmationReport) si.getReport()).getCustomerDetails();
			}
		};
		return attr.getInvoiceAddress();
	}

	public String getConfirmationReportNumbers() {
		String s = "";
		Set<String> nos = new HashSet<String>();
		for (ConfirmationItem ci:getConfirmationItems()){
			nos.add(ci.getReport().getDocumentNumber());
		}
		for (String no:nos){
			s += no + " ";
		}
		return s;
	}
	
	public String getDeliveryNotesNumbers() {
		String s = "";
		Set<String> nos = new HashSet<String>();
		for (ShippingItem ci:getShippingItems()){
			nos.add(ci.getReport().getDocumentNumber());
		}
		for (String no:nos){
			s += no + " ";
		}
		return s;
	}

}
