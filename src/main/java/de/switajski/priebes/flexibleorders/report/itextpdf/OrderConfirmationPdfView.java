package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.report.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.report.itextpdf.builder.PdfPTableBuilder;

@Component
public class OrderConfirmationPdfView extends PriebesIText5PdfView {

	//TODO: make VAT_RATE dependent from order
	public static final Double VAT_RATE = 0.19d;

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ConfirmationReport report = (ConfirmationReport) model.get(ConfirmationReport.class.getSimpleName());
		
		String expectedDelivery = "";
		if (report.getExpectedDelivery() != null)
			expectedDelivery = dateFormat.format(report.getExpectedDelivery().toString());
		//TODO implement Customer number
		String customerNumber = "";
		String created = dateFormat.format(report.getCreated());
		
        insertAdresse(document, report.getInvoiceAddress());
        
        //TODO: A-Umlaut wird nicht angezeigt
		insertHeading(document, "Auftragsbestätigung");
		
        CustomPdfPTableBuilder infoTableBuilder = CustomPdfPTableBuilder.createInfoTable(
        		report.getDocumentNumber().toString(),
        		created, expectedDelivery, customerNumber);
        
        insertInfoTable(document, infoTableBuilder);

        document.add(createTable(report));

		insertFooter(writer, report);
	}

	private PdfPTable createTable(ConfirmationReport cReport) throws DocumentException{
		PdfPTableBuilder builder = PdfPTableBuilder.buildWithSixCols();
		for (HandlingEvent he: cReport.getEvents()){
			List<String> list = new ArrayList<String>();
			// Art.Nr.:
			list.add(he.getOrderItem().getProduct().getProductNumber().toString());
			// Artikel
			list.add(he.getOrderItem().getProduct().getName());
			// Anzahl
			list.add(String.valueOf(he.getQuantity()));
			// EK per Stueck
			list.add(he.getOrderItem().getNegotiatedPriceNet().toString());
			// Bestellnr
			list.add(he.getOrderItem().getOrder().getOrderNumber());
			// gesamt
			list.add(he.getOrderItem().getNegotiatedPriceNet().multiply(he.getQuantity()).toString());
			
			builder.addBodyRow(list);
		}
		
		return builder.withFooter(false).build();
	}
	
}
