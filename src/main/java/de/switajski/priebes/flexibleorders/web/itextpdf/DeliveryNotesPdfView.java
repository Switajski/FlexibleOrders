package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.application.AgreementHistory;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.report.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
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
		
		DeliveryHistory history = DeliveryHistory.createWholeFrom(report);
		AgreementHistory aHistory = new AgreementHistory(history.getItems(AgreementItem.class));

		String date = "Lieferdatum: "
				+ dateFormat.format(report.getCreated());
		String packageNo = "Pakete: " + report.getDocumentNumber();
		String customerNo = "Kundennummer: " + report.getCustomerNumber();
		Address adresse = report.getShippedAddress();
		String heading = "Lieferschein " + report.getDocumentNumber();

		for (Paragraph p: ReportViewHelper.insertAddress(adresse)){
			document.add(p);
		};

		for (Paragraph p: ReportViewHelper.insertHeading(heading)){
			document.add(p);
		}

		CustomerDetails customerDetails = aHistory.getCustomerDetails();
		if (customerDetails == null){
			document.add(ReportViewHelper.insertInfoTable(
				packageNo, customerNo, null, date));
		}
		else {
		    PdfHelper.insertExtInfoTable(
                    customerDetails,
                    ExpectedDeliveryStringCreator.createDeliveryWeekString(aHistory.getAgreementDetails().getExpectedDelivery(), history),
                    aHistory.getAgreementDetails(),
                    date,
                    customerNo,
                    history.getOrderNumbers());
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
