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

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;

/**
 * Pdf view customized for the display of an order
 * 
 * @author Marek
 * 
 */
@Component
public class OrderPdfView extends PriebesIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Order report = (Order) model.get(Order.class.getSimpleName());

		String rightTop = "";
		String rightBottom = "Kundennummer: "
				+ report.getCustomer().getCustomerNumber();
		String leftTop = "Bestellnummer: " + report.getOrderNumber().toString();
		String leftBottom = "Bestelldatum: "
				+ dateFormat.format(report.getCreated());
		Address adresse = report.getCustomer().getAddress();
		String heading = "Bestellung";

		Amount netGoods = AmountCalculator.sum(AmountCalculator
				.getAmountsTimesQuantity(report));
		Amount vat = netGoods.multiply(Order.VAT_RATE);
		Amount gross = netGoods.add(vat);

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

		// insert footer table
		if (hasRecommendedPrices(report) && hasVat(report)) {
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

	}

	private PdfPTable createTable(Order order) throws DocumentException {

		PdfPTableBuilder builder = new PdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithFiveCols());
		for (OrderItem oi : order.getItems()) {
			if (!oi.isShippingCosts()) {
				List<String> row = new ArrayList<String>();
				row.add(oi.getProduct().getProductNumber().toString());
				// Artikel
				row.add(oi.getProduct().getName());
				// Anzahl
				row.add(String.valueOf(oi.getOrderedQuantity()));
				// EK per Stueck
				row.add(getPriceString(oi));
				// gesamt
				row.add(getPriceXquantity(oi));
				builder.addBodyRow(row);
			}
		}
		return builder.withFooter(false).build();
	}

	private String getPriceXquantity(OrderItem he) {
		if (he.getNegotiatedPriceNet() != null
				&& he.getNegotiatedPriceNet().getValue() != null)
			return he
					.getNegotiatedPriceNet()
					.multiply(he.getOrderedQuantity())
					.toString();
		else
			return "-";
	}

	private String getPriceString(OrderItem he) {
		String priceString = "";
		if (he.getNegotiatedPriceNet() != null
				&& he.getOrderedQuantity() != null
				&& he.getNegotiatedPriceNet().getValue() != null)
			priceString = he.getNegotiatedPriceNet().toString();
		else
			priceString = "Preis n. verf.";
		return priceString;
	}

	private boolean hasVat(Order order) {
		return !(order.getVatRate() == null);
	}

	private boolean hasRecommendedPrices(Order order) {
		for (OrderItem oi : order.getItems()) {
			if (oi.getNegotiatedPriceNet() == null
					|| oi.getNegotiatedPriceNet().getValue() == null)
				return false;
		}
		return true;
	}

}
