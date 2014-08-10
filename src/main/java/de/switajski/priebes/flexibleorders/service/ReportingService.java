package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.CreditNote;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;

@Service
public class ReportingService {

	@Autowired
	private ReportRepository reportRepo;

	public OrderConfirmation retrieveOrderConfirmation(String orderConfirmationNo){
		return retrieveReport(orderConfirmationNo, OrderConfirmation.class);
	}
	
	public OrderAgreement retrieveOrderAgreement(String orderAgreementNo){
		return retrieveReport(orderAgreementNo, OrderAgreement.class);
	}
	
	public DeliveryNotes retrieveDeliveryNotes(String deliveryNotesNo){
		return retrieveReport(deliveryNotesNo, DeliveryNotes.class);
	}
	
	public Invoice retrieveInvoice(String invoiceNo){
		return retrieveReport(invoiceNo, Invoice.class);
	}
	
	public CreditNote retrieveCreditNote(String creditNoteNo){
		return retrieveReport(creditNoteNo, CreditNote.class);
	}
	
	private <T extends Report> T retrieveReport(String documentNo, Class<T> type){
		Report r = reportRepo.findByDocumentNumber(documentNo);
		if (type.isInstance(r))
			return type.cast(r);
		else
			return null;
	}

}
