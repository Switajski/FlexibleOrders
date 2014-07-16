package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPCellBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;

@Component
public class ConfirmationReportPdfView extends PriebesIText5PdfView {

	// TODO: make VAT_RATE dependent from order
	public static final Double VAT_RATE = 0.19d;

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ConfirmationReport report = (ConfirmationReport) model
				.get(ConfirmationReport.class.getSimpleName());

		String heading = "Auftragsbest" + Unicode.aUml + "tigung";
		Address adresse = report.getInvoiceAddress();
		String documentNo = "Auftragsnummer: "
				+ report.getDocumentNumber().toString();
		Date expectedDelivery = report.getExpectedDelivery();

		String expectedDeliveryString = "";
		if (expectedDelivery != null)
			expectedDeliveryString = "voraus. Lieferung: KW "
					+ weekDateFormat.format(expectedDelivery);
		
		String date = "Auftragsdatum: "
				+ dateFormat.format(report.getCreated());
		String customerNo = "Kundennummer: " + report.getCustomerNumber();

		Amount netGoods = AmountCalculator.sum(AmountCalculator
				.getAmountsTimesQuantity(report));
		Amount vat = netGoods.multiply(report.getVatRate());
		Amount gross = netGoods.add(vat);

		for (Paragraph p : ReportViewHelper.insertAddress(report
				.getInvoiceAddress())) {
			document.add(p);
		}

		for (Paragraph p : ReportViewHelper.insertHeading(heading)) {
			document.add(p);
		}

		if (report.getCustomerDetails() == null){
			document.add(ReportViewHelper.insertInfoTable(
					customerNo,//rightTop, 
					expectedDeliveryString,//rightBottom, 
					documentNo,//leftTop, 
					date//leftBottom
					));
		} else {

			insertExtInfoTable(
					document,
					report,
					documentNo,
					expectedDeliveryString,
					date,
					customerNo);
		}
		

		document.add(ParagraphBuilder.createEmptyLine());

		// insert main table
		document.add(createTable(report));

		// insert footer table
		CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder
				.createFooterBuilder(
						netGoods, vat, null, gross, null)
				.withTotalWidth(PriebesIText5PdfView.WIDTH);

		PdfPTable footer = footerBuilder.build();

		footer.writeSelectedRows(0, -1,
				/* xPos */PriebesIText5PdfView.PAGE_MARGIN_LEFT,
				/* yPos */PriebesIText5PdfView.PAGE_MARGIN_BOTTOM
						+ FOOTER_MARGIN_BOTTOM,
				writer.getDirectContent());
	}

	private void insertExtInfoTable(Document document,
			ConfirmationReport report, String documentNo,
			String expectedDeliveryString, String date, String customerNo)
			throws DocumentException {
		PhraseBuilder pb = new PhraseBuilder("");
		PdfPCellBuilder cellb = new PdfPCellBuilder(new Phrase());
		
		Address shipAddr = report.getShippingAddress();
		String saleRepresentative = report.getCustomerDetails().getSaleRepresentative() == null ? "" : "Vertreter: " + report.getCustomerDetails().getSaleRepresentative();
		String contact1 = report.getCustomerDetails().getContact1() == null ? "" : report.getCustomerDetails().getContact1();
		String contact2 = report.getCustomerDetails().getContact2() == null ? "" : report.getCustomerDetails().getContact2();
		String contact3 = report.getCustomerDetails().getContact3() == null ? "" : report.getCustomerDetails().getContact3();
		String mark = report.getCustomerDetails().getMark() == null ? "" : "Ihr Zeichen: " + report.getCustomerDetails().getMark();
		String vendorno = report.getCustomerDetails().getVendorNumber() == null ? "" : "Lieferantennr.: " + report.getCustomerDetails().getVendorNumber();
		CustomPdfPTableBuilder infoTableBuilder = new CustomPdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithThreeCols())

				.addCell(cellb.withPhrase(
						pb.withText(date)
								.build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(customerNo).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(vendorno).build()).build())

				.addCell(cellb.withPhrase(
						pb.withText(documentNo).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText("Lieferadresse:").build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(saleRepresentative).build()).build())

				.addCell(cellb.withPhrase(
						pb.withText(expectedDeliveryString).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(" " + shipAddr.getName1() + " " + shipAddr.getName2()).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(contact1).build()).build())
		
				.addCell(cellb.withPhrase(
						pb.withText(" ").build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(" " + shipAddr.getStreet()).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(contact2).build()).build())
				
				.addCell(cellb.withPhrase(
						pb.withText(mark).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(" " + shipAddr.getPostalCode() + " " +  shipAddr.getCity()).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(contact3).build()).build());

		PdfPTable infoTable = infoTableBuilder.build();

		infoTable.setWidthPercentage(100);
		
		document.add(infoTable);
	}

	private PdfPTable createTable(ConfirmationReport cReport)
			throws DocumentException {
		PdfPTableBuilder builder = new PdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithSixCols());
		for (ReportItem he : cReport.getItemsOrdered()) {
			List<String> list = new ArrayList<String>();
			// Art.Nr.:
			Long pNo = he.getOrderItem().getProduct().getProductNumber();
			list.add(pNo.equals(0L) ? "n.a." : pNo.toString());
			// Artikel
			list.add(he.getOrderItem().getProduct().getName());
			// Anzahl
			list.add(String.valueOf(he.getQuantity()));
			// EK per Stueck
			list.add(he.getOrderItem().getNegotiatedPriceNet().toString());
			// Bestellnr
			list.add(he.getOrderItem().getOrder().getOrderNumber());
			// gesamt
			list.add(he
					.getOrderItem()
					.getNegotiatedPriceNet()
					.multiply(he.getQuantity())
					.toString());

			builder.addBodyRow(list);
		}

		return builder.withFooter(false).build();
	}

}
