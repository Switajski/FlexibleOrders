package de.switajski.priebes.flexibleorders.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

@Service
public class PurchaseAgreementService {

	@Transactional(readOnly = true)
	public Set<PurchaseAgreement> retrieve(Collection<ReportItem> reportItems) {
		boolean agreedOnly = false;
		return retrieve(reportItems, agreedOnly);
	}

	/**
	 * retrieve only purchase agreements that are agreed by an order agreement
	 * 
	 * @param reportItems works with confirmation items only.
	 * @return
	 */
	@Transactional(readOnly = true)
	public Set<PurchaseAgreement> retrieveLegal(
			Collection<ReportItem> reportItems) {
		boolean agreedOnly = true;
		return retrieve(reportItems, agreedOnly);
	}
	
	/**
	 * 
	 * @param reportItems works with confirmation items only
	 * @param agreedOnly
	 * @return
	 */
	private Set<PurchaseAgreement> retrieve(Collection<ReportItem> reportItems,
			boolean agreedOnly) {
		DeliveryHistory dh = new DeliveryHistory(reportItems);
		Set<PurchaseAgreement> ocs = new HashSet<PurchaseAgreement>();
		for (ConfirmationItem cis : dh.getReportItems(ConfirmationItem.class)) {
			OrderConfirmation orderConfirmation = (OrderConfirmation) cis
					.getReport();
			if (agreedOnly) {
				if (orderConfirmation.isAgreed())
					ocs.add(orderConfirmation.getPurchaseAgreement());
			} else {
				ocs.add(orderConfirmation.getPurchaseAgreement());
			}
		}
		return ocs;
	}
	
	/**
	 * 
	 * @param reportItems
	 * @return
	 * @throws IllegalStateException if retrieved purchase agreement is not one
	 */
	public PurchaseAgreement retrieveSingle(Collection<ReportItem> reportItems){
		Set<PurchaseAgreement> pas = retrieve(reportItems);
		return extractOneOrFail(pas);
	}

	private PurchaseAgreement extractOneOrFail(Set<PurchaseAgreement> pas) {
		if (pas.isEmpty()){
			throw new IllegalStateException("No purchase agreement found");
		} else if (pas.size() == 1){
			return pas.iterator().next();
		} else {
			throw new IllegalStateException("Mehere AB#s vorhanden - kann keinen einzelnen Kaufvertrag ausmachen");
		}
	}

	public PurchaseAgreement retrieveSingle(Report report) {
		DeliveryHistory dh = retrieveFromService(report);
		Set<PurchaseAgreement> pas = retrievePurchaseAgreements(dh);
		
		return extractOneOrFail(pas);
	}

	/**
	 * FIXME: won't work - it will also retrieve weird order confirmations, that have nothing to do with a invoice.
	 * Report item needs predecessors
	 * 
	 * @param report
	 * @return
	 */
	private DeliveryHistory retrieveFromService(Report report) {
		// in eine DeliveryHistoryService mit create(Report)
		Set<ReportItem> ris = new HashSet<ReportItem>();
		for (ReportItem ri:report.getItems()){
			ri.getOrderItem().getReportItems();
		}
		DeliveryHistory dh = new DeliveryHistory(ris);
		return dh;
	}
	
	public Set<PurchaseAgreement> retrievePurchaseAgreements(DeliveryHistory dh){
    	Set<PurchaseAgreement> pas = new HashSet<PurchaseAgreement>();
    	
    	Set<OrderConfirmation> cis = dh.getReports(OrderConfirmation.class);
    	for (OrderConfirmation oc : cis){
    		pas.add(oc.getPurchaseAgreement());
    	}
    	
    	return pas;
    }


}
