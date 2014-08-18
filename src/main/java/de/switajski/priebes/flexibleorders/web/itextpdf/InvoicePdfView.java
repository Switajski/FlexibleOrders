package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.application.AgreementHistory;
import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPCellBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;
import de.switajski.priebes.flexibleorders.reference.ProductType;

@Component
public class InvoicePdfView extends PriebesIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Invoice report = (Invoice) model.get(Invoice.class.getSimpleName());
		DeliveryHistory deliveryHistory = DeliveryHistory.createWholeFrom(report);
		AgreementHistory aHistory = new AgreementHistory(deliveryHistory);

		String deliveryDate = hasItemsWithDifferentCreationDates(report) ? 
				"" : "Lieferdatum: " + dateFormat.format(getDeliveryNotesDate(report));
		String customerNo = "Kundennummer: " + report.getCustomerNumber();
		String documentNo = "Rechnungsnummer: "
				+ report.getDocumentNumber().toString();
		String date = "Rechnungsdatum: "
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

		for (Paragraph p: ReportViewHelper.insertAddress(adresse)){
			document.add(p);
		};

		for (Paragraph p: ReportViewHelper.insertHeading(heading)){
			document.add(p);
		}


		PhraseBuilder pb = new PhraseBuilder("");
		PdfPCellBuilder cellb = new PdfPCellBuilder(new Phrase());

		String orderConfirmationNumbers = "Auftragsnr.: " + deliveryHistory.getConfirmationReportNumbers();
		String vendorNo = "";
		String atu = "";
		CustomerDetails customerDetails = aHistory.getCustomerDetails();
		if (customerDetails != null){
			vendorNo = customerDetails == null ? "" : "Lieferantennr.: " + customerDetails.getVendorNumber();
			atu = customerDetails.getVatIdNo() == null ? "" : "ATU: " + customerDetails.getVatIdNo();
		}
		String billing = StringUtils.isEmpty(report.getBilling()) ? "" : "Abrechnung: " + report.getBilling();

		CustomPdfPTableBuilder infoTableBuilder = new CustomPdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithThreeCols())

		.addCell(cellb.withPhrase(
			pb.withText(date).build()).build())
		.addCell(cellb.withPhrase(
			pb.withText(customerNo).build()).build())
		.addCell(cellb.withPhrase(
			pb.withText(vendorNo).build()).build())

		.addCell(cellb.withPhrase(
			pb.withText(documentNo).build()).build())
		.addCell(cellb.withPhrase(
			pb.withText(atu).build()).build())
		.addCell(cellb.withPhrase(
			pb.withText("").build()).build())
		
		.addCell(cellb.withPhrase(
			pb.withText("").build()).build())
		.addCell(cellb.withPhrase(
			pb.withText(orderConfirmationNumbers).build()).build())
		.addCell(cellb.withPhrase(
			pb.withText(billing).build()).build());

		PdfPTable infoTable = infoTableBuilder.build();

		infoTable.setWidthPercentage(100);

		document.add(infoTable);

		
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
		Date deliveryNotesDate;
		try {
			DeliveryHistory deliveryHistory = DeliveryHistory.createFrom(report.getItems().iterator().next().getOrderItem());
			deliveryNotesDate = deliveryHistory.getItems(ShippingItem.class).iterator().next().getCreated();
			return deliveryNotesDate;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		return new Date();
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
			Set<ShippingItem> sis = DeliveryHistory.createFrom(ii.getOrderItem()).getItems(ShippingItem.class);
			
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
		
		try {
			Set<ShippingItem> shippingItems = invoice.getItems().iterator().next().getOrderItem().getShippingItems();
			Date firstDate = shippingItems.iterator().next().getCreated();
			for (ShippingItem si: shippingItems){
				if (!DateUtils.isSameDay(si.getDeliveryNotes().getCreated(), firstDate))
					return true;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

}
