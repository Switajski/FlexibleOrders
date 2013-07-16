package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.report.OrderConfirmation;

@Component
public class OrderConfirmationPdfView extends PriebesIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		OrderConfirmation auftragsbestaetigung =  (OrderConfirmation) model.get("OrderConfirmation");
		insertHeader(document);
        insertAdresse(document, auftragsbestaetigung.getCustomer().getInvoiceAddress());
        insertSubject(document,"Auftragsbestätigung Nr." 
        		//TODO: implement OrderConfirmationNumber
//        		+ auftragsbestaetigung.getAbnr().toString()
        		);
        insertInfo(document,"Auftragsdatum: " + dateFormat.format(auftragsbestaetigung.getCreated()));
        //TODO: if (auftragsbestaetigung.getAusliefDatum==null) insertInfo(document,"Voraussichtliches Auslieferungsdatum:" + auftragsbestaetigung.getGeplAusliefDatum());
        this.insertEmptyLines(document, 2);
        document.add(new OrderConfirmationPdfTable(auftragsbestaetigung, auftragsbestaetigung.getItems()));

	}

}
