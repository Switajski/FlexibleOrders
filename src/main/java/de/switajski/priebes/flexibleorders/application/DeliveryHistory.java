package de.switajski.priebes.flexibleorders.application;

import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.CancellationItem;
import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

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
	
	public Set<ReportItem> getItems(){
		return reportItems;
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

}
