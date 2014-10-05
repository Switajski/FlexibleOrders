package de.switajski.priebes.flexibleorders.web;

import java.util.HashMap;
import java.util.Map;

import de.switajski.priebes.flexibleorders.domain.report.CreditNote;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.web.itextpdf.CreditNotePdfView;
import de.switajski.priebes.flexibleorders.web.itextpdf.DeliveryNotesPdfView;
import de.switajski.priebes.flexibleorders.web.itextpdf.InvoicePdfView;
import de.switajski.priebes.flexibleorders.web.itextpdf.OrderAgreementPdfView;
import de.switajski.priebes.flexibleorders.web.itextpdf.OrderConfirmationPdfView;

public class ModelToPdfView {

	private static final Map<String, String> modelToView = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(OrderConfirmation.class.getSimpleName(),
					OrderConfirmationPdfView.class.getSimpleName());
			put(OrderAgreement.class.getSimpleName(),
					OrderAgreementPdfView.class.getSimpleName());
			put(DeliveryNotes.class.getSimpleName(),
					DeliveryNotesPdfView.class.getSimpleName());
			put(Invoice.class.getSimpleName(),
					InvoicePdfView.class.getSimpleName());
			put(CreditNote.class.getSimpleName(),
					CreditNotePdfView.class.getSimpleName());
		}
	};
	
	public static String getView(String model) {
        String view = modelToView.get(model);
        if (view == null) throw new IllegalStateException(
                "There is no view for given model defined");
        return view;
    }
}
