package de.switajski.priebes.flexibleorders.application;

import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;

public class DeliveryHistoryFactory {

	public static DeliveryHistory create(ReportItem reportItem){
		return new DeliveryHistory(reportItem.getOrderItem().getReportItems());
	}
	
	public static DeliveryHistory create(OrderItem oi){
		return new DeliveryHistory(oi.getReportItems());
	}

	public static DeliveryHistory createFromFirst(Set<ReportItem> items) {
		return create(items.iterator().next().getOrderItem());
	}

	public static DeliveryHistory createFromReport(Report report) {
		Set<ReportItem> allRis = new HashSet<ReportItem>(); 
		for (ReportItem ri : report.getItems()){
			allRis.addAll(ri.getOrderItem().getReportItems());
		}
		return new DeliveryHistory(allRis);
	}
}
