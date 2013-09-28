package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class ArchivePdfView extends PriebesIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//TODO: Archiv dokument erstellen
		/*Archive bestellung =  (Archive) model.get("Archive");
        insertAdresse(document, bestellung.getCustomer().getInvoiceAddress());
        insertSubject(document,"Archiv Nr." + bestellung.getOrderNumber());
        insertInfo(document,"Archiviert am: " + dateFormat.format(bestellung.getCreated()));
        this.insertEmptyLines(document, 1);
        document.add(new ArchivPdfTable(bestellung));*/

	}

}
