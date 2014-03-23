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

import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.report.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.report.itextpdf.builder.PdfPTableBuilder;

@Component
public class DeliveryNotesPdfView extends PriebesIText5PdfView {

	private static final double VAT_RATE = 0.19d;

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		DeliveryNotes report = (DeliveryNotes) model.get(DeliveryNotes.class.getSimpleName());
		
		String expectedDelivery = "";
		//TODO implement Customer number
		String customerNumber = "";
		String created = dateFormat.format(report.getCreated());
		
        insertAdresse(document, report.getShippedAddress());
        
        //TODO: A-Umlaut wird nicht angezeigt
		insertHeading(document, "Lieferschein");
		
        CustomPdfPTableBuilder infoTableBuilder = CustomPdfPTableBuilder.createInfoTable(
        		report.getDocumentNumber().toString(),
        		created, expectedDelivery, customerNumber);
        
        insertInfoTable(document, infoTableBuilder);

        document.add(createTable(report));

		insertFooter(writer, report);
	}
	
//	private PdfPTable createTable(DeliveryNotes deliveryNotes, Document doc) throws DocumentException{
//		PdfPTableBuilder builder = PdfPTableBuilder.buildWithFourCols();
//		for (HandlingEvent he: deliveryNotes.getEvents()){
//			if (!he.getOrderItem().isShippingCosts()){
//				builder.addBodyRow(
//						new FourStrings("",
//								// product Name
//								"Art.Nr.: " + he.getOrderItem().getProduct().getProductNumber() + " - "
//								+ he.getOrderItem().getProduct().getName(),
//								// price
//								he.getQuantity()+ " x " + he.getOrderItem().getNegotiatedPriceNet().toString(),
//								// amount of single item
//								he.getOrderItem().getNegotiatedPriceNet().multiply(he.getQuantity()).toString()
//								));
//			}
//		}
//		Amount net = AmountCalculator.calculateNetAmount(deliveryNotes);
//		//TODO: make vatRate dependent from order
//		Amount vat = AmountCalculator.calculateVatAmount(deliveryNotes, VAT_RATE);
//		builder.addFooterRow("Warenwert netto:   "+ net.toString())
//		.addFooterRow("zzgl. 19% MwSt.:     " + vat.toString())
////		.addFooterRow("Versandkosten:     " + deliveryNotes.getShipment().getShippingCosts().toString())
//		.addFooterRow("Gesamtbetrag brutto:   " + net.add(vat));
//		return builder.build();
//	}
	
	private PdfPTable createTable(Report cReport) throws DocumentException{
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
