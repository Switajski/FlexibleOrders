package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class ReportBuilder<T extends ReportBuilder> {

	protected String documentNumber;
	protected Set<ReportItem> items = new HashSet<ReportItem>();
	protected Long customerNumber;
	
	public T setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
		return (T) this;
	}
	public T setItems(Set<ReportItem> items) {
		this.items = items;
		return (T) this;
	}
	public T setCustomerNumber(Long customerNumber) {
		this.customerNumber = customerNumber;
		return (T) this;
	}
	protected Report build(Report report){
		report.setCustomerNumber(customerNumber);
		report.setDocumentNumber(documentNumber);
		for (ReportItem item:items)
			report.addItem(item);
		return report;
	}
}
