package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.helper.AmountCalculator;
import de.switajski.priebes.flexibleorders.report.itextpdf.builder.FourStrings;
import de.switajski.priebes.flexibleorders.report.itextpdf.builder.PdfPTableBuilder;

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
		
		FlexibleOrder bestellung =  (FlexibleOrder) model.get(FlexibleOrder.class.getSimpleName());
        insertAdresse(document, bestellung.getCustomer().getAddress());
        insertSubject(document,"Bestellung Nr." + bestellung.getOrderNumber());
        insertInfo(document,"Bestelldatum: " + dateFormat.format(bestellung.getCreated()));
        this.insertEmptyLines(document, 1);
        document.add(createTable(bestellung));

	}
	
	private PdfPTable createTable(FlexibleOrder order){
		
		PdfPTableBuilder builder = new PdfPTableBuilder()
		.setHeader(new FourStrings("Bestellpos.", "Artikel", "Menge x Preis", "Betrag"));
		for (OrderItem he: order.getItems()){
			String priceString = getPriceString(he);
			String priceXquantity = getPriceXquantity(he);
			builder.addBodyRow(
				new FourStrings("",
				// product Name
				"Art.Nr.: " + he.getProduct().getProductNumber() + " - "
				+ he.getProduct().getName(),
				// price
				priceString,
				// amount of single item
				priceXquantity
			));
		}
		if (hasRecommendedPrices(order) && hasVat(order)){
			Amount net = AmountCalculator.calculateNetAmount(order);
			Amount vat = AmountCalculator.calculateVatAmount(order, order.getVatRate());
			builder.addFooterRow("Warenwert netto:   " + net.toString())
			.addFooterRow("zzgl. " + order.getVatRate() + "% MwSt.   " + vat.toString())
			.addFooterRow("Gesamtbetrag brutto:   " + net.add(vat).toString());
		} else {
			builder.addFooterRow("Warenwert netto:   " + "           -    ");
		}
		return builder.build();
	}

	private String getPriceXquantity(OrderItem he) {
		if (he.getNegotiatedPriceNet() != null && he.getNegotiatedPriceNet().getValue() != null)
			return he.getNegotiatedPriceNet().multiply(he.getOrderedQuantity()).toString();
		else 
			return " -     ";
	}

	private String getPriceString(OrderItem he) {
		String priceString ="";
		if (he.getNegotiatedPriceNet() != null && he.getOrderedQuantity() != null
				&& he.getNegotiatedPriceNet().getValue() != null)
			priceString = he.getOrderedQuantity()+ " x " + he.getNegotiatedPriceNet().toString();
		else 
			priceString = "Preis n. verf.";
		return priceString;
	}
	
	private boolean hasVat(FlexibleOrder order) {
		return !(order.getVatRate() == null);
	}

	private boolean hasRecommendedPrices(FlexibleOrder order) {
		for (OrderItem oi:order.getItems()){
			if (oi.getNegotiatedPriceNet()==null || oi.getNegotiatedPriceNet().getValue()==null)
				return false;
		}
		return true;
	}

}
