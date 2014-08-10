package de.switajski.priebes.flexibleorders.application.process;

import java.util.ArrayList;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.domain.ReceiptItem;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

public class WholesaleProcess {
	
	public static List<Class<? extends ReportItem>> reportItemOrder(){
		List<Class<? extends ReportItem>> reportItemOrder = new ArrayList<Class<? extends ReportItem>>();
		reportItemOrder.add(ConfirmationItem.class);
		reportItemOrder.add(AgreementItem.class);
		reportItemOrder.add(ShippingItem.class);
		reportItemOrder.add(InvoiceItem.class);
		reportItemOrder.add(ReceiptItem.class);
		return reportItemOrder;
	}
	
	public static List<Class<? extends Report>> reportOrder(){
		List<Class<? extends Report>> reportOrder = new ArrayList<Class<? extends Report>>();
		reportOrder.add(OrderConfirmation.class);
		reportOrder.add(OrderAgreement.class);
		reportOrder.add(DeliveryNotes.class);
		reportOrder.add(Invoice.class);
		reportOrder.add(Receipt.class);
		return reportOrder;
	}
	
	public static Class<? extends ReportItem> getReportItemStepBefore(Class<? extends ReportItem> clazz){
		int index = reportItemOrder().indexOf(clazz);
		if (index > 0)
			return reportItemOrder().get(index--);
		else return null;
	}
	
	public static Class<? extends Report> getReportBefore(Class<? extends Report> clazz){
		int index = reportOrder().indexOf(clazz);
		if (index > 0)
			return reportOrder().get(index--);
		else 
			return null;
	}
	
}
