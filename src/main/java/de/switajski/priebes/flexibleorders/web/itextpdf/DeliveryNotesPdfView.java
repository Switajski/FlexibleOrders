package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;

@Component
public class DeliveryNotesPdfView extends PriebesIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DeliveryNotes report = (DeliveryNotes) model.get(DeliveryNotes.class
				.getSimpleName());

		String leftTop = "Lieferscheinnummer: "
				+ report.getDocumentNumber().toString();
		String leftBottom = "Lieferdatum: "
				+ dateFormat.format(report.getCreated());
		String rightTop = "";
		// TODO
		String rightBottom = "Kundennummer: " + report.getCustomerNumber();
		Address adresse = report.getShippedAddress();
		String heading = "Lieferschein";

		// insert address
		document.add(ParagraphBuilder.createEmptyLine());
		document.add(ParagraphBuilder.createEmptyLine());
		document.add(ParagraphBuilder.createEmptyLine());
		document.add(ParagraphBuilder.createEmptyLine());
		if (adresse == null) {
			document.add(ParagraphBuilder.createEmptyLine());
			document.add(ParagraphBuilder.createEmptyLine());
			document.add(ParagraphBuilder.createEmptyLine());
		} else {
			document.add(new ParagraphBuilder(adresse.getName1())
					.withIndentationLeft(36f)
					.withLineSpacing(12f)
					.addTextLine(adresse.getName2())
					.addTextLine(adresse.getStreet())
					.addTextLine(
							adresse.getPostalCode() + " " + adresse.getCity())
					.addTextLine(adresse.getCountry().toString())
					.build());
		}
		document.add(ParagraphBuilder.createEmptyLine());
		document.add(ParagraphBuilder.createEmptyLine());

		// insert heading
		document.add(new ParagraphBuilder(heading)
				.withFont(FontFactory.getFont(FONT, 12, Font.BOLD))
				.build());
		document.add(ParagraphBuilder.createEmptyLine());

		// info table
		CustomPdfPTableBuilder infoTableBuilder = CustomPdfPTableBuilder
				.createInfoTable(
						leftTop, leftBottom, rightTop, rightBottom);
		PdfPTable infoTable = infoTableBuilder.build();
		infoTable.setWidthPercentage(100);
		document.add(infoTable);
		// TODO: if (auftragsbestaetigung.getAusliefDatum==null)
		// insertInfo(document,"Voraussichtliches Auslieferungsdatum:" +
		// auftragsbestaetigung.getGeplAusliefDatum());
		document.add(ParagraphBuilder.createEmptyLine());

		// insert main table
		document.add(createTable(report));

	}

	private PdfPTable createTable(Report cReport) throws DocumentException {
		PdfPTableBuilder builder = new PdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithFourCols());
		for (ReportItem he : cReport.getItems()) {
			if (!he.getOrderItem().isShippingCosts()) {
				List<String> row = new ArrayList<String>();
				// Anzahl
				row.add(String.valueOf(he.getQuantity()) + " x ");
				// Art.Nr.:
				row.add(he
						.getOrderItem()
						.getProduct()
						.getProductNumber()
						.toString());
				// Artikel
				row.add(he.getOrderItem().getProduct().getName());
				// Bestellnr
				row.add(he.getOrderItem().getOrder().getOrderNumber());

				builder.addBodyRow(row);
			}
		}

		return builder.withFooter(false).build();
	}

}
