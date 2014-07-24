package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.application.DeliveryHistoryFactory;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPCellBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;

@Component
public class DeliveryNotesPdfView extends PriebesIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DeliveryNotes report = (DeliveryNotes) model.get(DeliveryNotes.class
				.getSimpleName());

		String documentNo = "Lieferscheinnummer: "
				+ report.getDocumentNumber().toString();
		String date = "Lieferdatum: "
				+ dateFormat.format(report.getCreated());
		String packageNo = "Pakete: " + report.getDocumentNumber();
		String customerNo = "Kundennummer: " + report.getCustomerNumber();
		Address adresse = report.getShippedAddress();
		String heading = "Lieferschein";

		for (Paragraph p: ReportViewHelper.insertAddress(adresse)){
			document.add(p);
		};

		for (Paragraph p: ReportViewHelper.insertHeading(heading)){
			document.add(p);
		}

		CustomerDetails customerDetails = DeliveryHistoryFactory.createFromFirst(report.getItems()).getCustomerDetails();
		if (customerDetails == null){
			document.add(ReportViewHelper.insertInfoTable(
				packageNo, customerNo, documentNo, date));
		}
		else {
			PhraseBuilder pb = new PhraseBuilder("");
			PdfPCellBuilder cellb = new PdfPCellBuilder(new Phrase());

			String packageNumber = StringUtils.isEmpty(report.getPackageNumber()) ? "" : "Paket(e): " + report.getPackageNumber();
			
			CustomPdfPTableBuilder infoTableBuilder = new CustomPdfPTableBuilder(
					PdfPTableBuilder.createPropertiesWithThreeCols())

					.addCell(cellb.withPhrase(
							pb.withText(date)
									.build()).build())
					.addCell(cellb.withPhrase(
							pb.withText(customerNo).build()).build())
					.addCell(cellb.withPhrase(
							pb.withText(customerDetails.getVendorNumber()).build()).build())
							
					.addCell(cellb.withPhrase(
							pb.withText(documentNo)
									.build()).build())
					.addCell(cellb.withPhrase(
							pb.withText(DeliveryHistoryFactory.createFromFirst(report.getItems()).getOrderConfirmationNumbers()).build()).build())
					.addCell(cellb.withPhrase(
							pb.withText("").build()).build())

					.addCell(cellb.withPhrase(
							pb.withText(packageNumber).build()).build())
					.addCell(cellb.withPhrase(
							pb.withText("").build()).build())
					.addCell(cellb.withPhrase(
							pb.withText("").build()).build());

			PdfPTable infoTable = infoTableBuilder.build();

			infoTable.setWidthPercentage(100);
			
			document.add(infoTable);
		}
		
		document.add(ParagraphBuilder.createEmptyLine());
		// insert main table
		document.add(createTable(report));

	}

	private PdfPTable createTable(Report cReport) throws DocumentException {
		PdfPTableBuilder builder = new PdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithFourCols());
		for (ReportItem he : cReport.getItemsOrdered()) {
			if (!he.getOrderItem().isShippingCosts()) {
				List<String> row = new ArrayList<String>();
				// Anzahl
				row.add(String.valueOf(he.getQuantity()) + " x ");
				// Art.Nr.:
				Long pNo = he.getOrderItem().getProduct().getProductNumber();
				row.add(pNo.equals(0L) ? "n.a." : pNo.toString());
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
