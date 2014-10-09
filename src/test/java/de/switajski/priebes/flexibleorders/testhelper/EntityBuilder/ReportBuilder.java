package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public abstract class ReportBuilder<BEAN extends Report, BUILDER extends Builder<BEAN>> implements Builder<BEAN>{

	protected String documentNumber;
	protected Set<ReportItem> items = new HashSet<ReportItem>();
	
	public ReportBuilder<BEAN, BUILDER> setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
		return this;
	}
	public ReportBuilder<BEAN, BUILDER> setItems(Set<ReportItem> items) {
		this.items = items;
		return this;
	}
	public BEAN build(BEAN report){
		report.setDocumentNumber(documentNumber);
		for (ReportItem item:items){
			report.addItem(item);
		}
		return report;
	}
}
