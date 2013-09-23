package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.report.Invoice;

@Component
public class InvoicePdfView extends PriebesIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Invoice invoice =  (Invoice) model.get("Invoice");
		insertHeader(document);
        insertAdresse(document, invoice.getCustomer().getInvoiceAddress());
        insertSubject(document, "Rechnung Nr." + invoice.getInvoiceNumber()
        		//TODO: implement Invoice nr.
//        		+ invoice.getInvoiceNumber().toString()
        		);
        this.insertEmptyLines(document, 2);
        InvoicePdfTable invoicePdfTable = new InvoicePdfTable(invoice, invoice.getItems());
        invoicePdfTable.setFirstColumnName("Bestellnr.");
        document.add(invoicePdfTable.build());

	}

}
