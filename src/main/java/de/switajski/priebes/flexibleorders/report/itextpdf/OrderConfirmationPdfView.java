package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.OrderConfirmation;

@Component
public class OrderConfirmationPdfView extends PriebesIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		OrderConfirmation orderConfirmation =  (OrderConfirmation) model.get("OrderConfirmation");
		insertHeader(document);
        insertAdresse(document, orderConfirmation.getCustomer().getInvoiceAddress());
        insertSubject(document,"Auftragsbestätigung Nr." 
        		+ orderConfirmation.getOrderConfirmationNumber());
        insertInfo(document,"Auftragsdatum: " + dateFormat.format(orderConfirmation.getCreated()));
        //TODO: if (auftragsbestaetigung.getAusliefDatum==null) insertInfo(document,"Voraussichtliches Auslieferungsdatum:" + auftragsbestaetigung.getGeplAusliefDatum());
        this.insertEmptyLines(document, 2);
        OrderConfirmationPdfTable table = new OrderConfirmationPdfTable(orderConfirmation, orderConfirmation.getItems());
        table.setFirstColumnName("Bestellnr.");
        document.add(table.build());

	}

}
