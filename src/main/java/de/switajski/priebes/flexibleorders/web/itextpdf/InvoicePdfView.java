package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.application.AgreementHistory;
import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;

@Component
public class InvoicePdfView extends PriebesIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Invoice report = (Invoice) model.get(Invoice.class.getSimpleName());
		DeliveryHistory history = DeliveryHistory.createWholeFrom(report);
		AgreementHistory aHistory = new AgreementHistory(history);

		String deliveryDate = hasItemsWithDifferentCreationDates(report) ? 
				"" : "Lieferscheindatum: " + dateFormat.format(getDeliveryNotesDate(report));//TODO refactor
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

		for (Paragraph p: ReportViewHelper.createAddress(adresse))
			document.add(p);

		document.add(ReportViewHelper.createDate(date));
		
		for (Paragraph p: ReportViewHelper.createHeading(heading))
			document.add(p);


		ExtInfoTableParameter param = new ExtInfoTableParameter();
        ReportViewHelper.mapDocumentNumbersToParam(history, param);
        param.customerDetails = aHistory.getCustomerDetails();
        param.date = date;
        param.customerNo = customerNo;
        param.agreementDetails = aHistory.getAgreementDetails();
        param.billing = StringUtils.isEmpty(report.getBilling()) ? "" : "Abrechnung: " + report.getBilling();

        document.add(ReportViewHelper.createExtInfoTable(param));

        document.add(ParagraphBuilder.createEmptyLine());

		// insert main table
		if (hasItemsWithDifferentCreationDates(report))
			document.add(createTableWithDeliveryNotes(report.getItems()));
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
	
	public static PdfPTable createTableWithDeliveryNotes(Collection<ReportItem> cReport) throws DocumentException {
		PdfPTableBuilder builder = new PdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithSevenCols());
		for (ReportItem ii : cReport) {
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
				list.add(ReportViewHelper.documentNumbersOf(sis));
				// Lieferdatum
				list.add(ReportViewHelper.createdDatesOf(sis));
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
