package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.reference.ProductType;

@Component
public class InvoicePdfView extends PriebesIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Invoice report = (Invoice) model.get(Invoice.class.getSimpleName());

		String rightTop = hasItemsWithDifferentCreationDates(report) ? "" : "Lieferdatum: " + dateFormat.format(getDeliveryNotesDate(report));
		String rightBottom = "Kundennummer: " + report.getCustomerNumber();
		String leftTop = "Rechnungsnummer: "
				+ report.getDocumentNumber().toString();
		String leftBottom = "Rechnungsdatum: "
				+ dateFormat.format(report.getCreated());
		Address adresse = report.getInvoiceAddress();
		String heading = "Rechnung";

		Amount shippingCosts = Amount.ZERO_EURO;
		if (report.getShippingCosts() != null)
			shippingCosts = report.getShippingCosts();

		Amount netGoods = AmountCalculator.sum(AmountCalculator
				.getAmountsTimesQuantity(report));
		Amount vat = (netGoods.add(shippingCosts))
				.multiply(report.getVatRate());
		Amount gross = netGoods.add(vat).add(shippingCosts);

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
		if (hasItemsWithDifferentCreationDates(report))
			document.add(createTableWithDeliveryNotes(report));
		else
			document.add(createTable(report));

		// insert footer table
		CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder
				.createFooterBuilder(
						netGoods,
						vat,
						shippingCosts,
						gross,
						report.getPaymentConditions())
				.withTotalWidth(PriebesIText5PdfView.WIDTH);

		PdfPTable footer = footerBuilder.build();

		footer.writeSelectedRows(0, -1,
				/* xPos */PriebesIText5PdfView.PAGE_MARGIN_LEFT,
				/* yPos */PriebesIText5PdfView.PAGE_MARGIN_BOTTOM
						+ FOOTER_MARGIN_BOTTOM,
				writer.getDirectContent());
	}

	private Date getDeliveryNotesDate(Invoice report) {
		DeliveryHistory deliveryHistory = report.getItems().iterator().next().getOrderItem().getDeliveryHistory();
		Date deliveryNotesDate = deliveryHistory.getShippingItems().iterator().next().getCreated();
		return deliveryNotesDate;
	}

	private PdfPTable createTable(Report cReport) throws DocumentException {
		PdfPTableBuilder builder = new PdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithSixCols());
		for (ReportItem he : cReport.getItemsOrdered()) {
			if (he.getOrderItem().getProduct().getProductType() != ProductType.SHIPPING) {
				List<String> list = new ArrayList<String>();
				// Art.Nr.:
				Long pNo = he.getOrderItem().getProduct().getProductNumber();
				list.add(pNo == null || pNo.equals(0L) ? "n.a." : pNo.toString());
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
		}

		return builder.withFooter(false).build();
	}
	
	private PdfPTable createTableWithDeliveryNotes(Report cReport) throws DocumentException {
		PdfPTableBuilder builder = new PdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithSevenCols());
		for (ReportItem ii : cReport.getItemsOrdered()) {
			Set<ShippingItem> sis = ii.getOrderItem().getDeliveryHistory().getShippingItems();
			
			if (ii.getOrderItem().getProduct().getProductType() != ProductType.SHIPPING) {
				List<String> list = new ArrayList<String>();
				// Art.Nr.:
				Long pNo = ii.getOrderItem().getProduct().getProductNumber();
				list.add(pNo == null || pNo.equals(0L) ? "n.a." : pNo.toString());
				// Artikel
				list.add(ii.getOrderItem().getProduct().getName());
				// Anzahl
				list.add(String.valueOf(ii.getQuantity()));
				// EK per Stueck
				list.add(ii.getOrderItem().getNegotiatedPriceNet().toString());
				// Lieferscheinnr.
				list.add(documentNumbersOf(sis));
				// Lieferdatum
				list.add(createdDatesOf(sis));
				// gesamt
				list.add(ii
						.getOrderItem()
						.getNegotiatedPriceNet()
						.multiply(ii.getQuantity())
						.toString());

				builder.addBodyRow(list);
			}
		}

		return builder.withFooter(false).build();
	}

	//TODO: Move to Util
	private String createdDatesOf(Set<ShippingItem> sis) {
		String createdDates = "";
		Iterator<ShippingItem> itr = sis.iterator();
		while (itr.hasNext()){
			ShippingItem si = itr.next();
			createdDates+= dateFormat.format(si.getReport().getCreated());
			if (itr.hasNext())
				createdDates += " ";
			
		}
		return createdDates;
	}

	//TODO: Move to Util
	private String documentNumbersOf(Set<ShippingItem> sis) {
		String documentNumbers = "";
		Iterator<ShippingItem> itr = sis.iterator();
		while (itr.hasNext()){
			ShippingItem si = itr.next();
			documentNumbers+= si.getReport().getDocumentNumber();
			if (itr.hasNext())
				documentNumbers += ", ";
			
		}
		return documentNumbers;
	}

	public boolean hasItemsWithDifferentCreationDates(Invoice invoice){
		Set<ShippingItem> shippingItems = invoice.getItems().iterator().next().getOrderItem().getShippingItems();
		Date firstDate = shippingItems.iterator().next().getCreated();
		for (ShippingItem si: shippingItems){
			if (!DateUtils.isSameDay(si.getDeliveryNotes().getCreated(), firstDate))
				return true;
		}
		return false;
	}

}
