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

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.helper.AmountCalculator;
import de.switajski.priebes.flexibleorders.report.itextpdf.builder.ParagraphBuilder;
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
        document.add(ParagraphBuilder.createEmptyLine());
        document.add(createTable(bestellung, document));

	}
	
	private PdfPTable createTable(FlexibleOrder order,Document document) throws DocumentException{
		
		PdfPTableBuilder builder = PdfPTableBuilder.buildWithFourCols();
		for (OrderItem he: order.getItems()){
			if (!he.isShippingCosts()){
				String priceString = getPriceString(he);
				String priceXquantity = getPriceXquantity(he);
				List<String> strings = new ArrayList<String>();
				strings.add("");
				strings.add("Art.Nr.: " + he.getProduct().getProductNumber() + " - "+ he.getProduct().getName());
				strings.add(priceString);
				strings.add(priceXquantity);
				builder.addBodyRow(strings);
			}
		}
		if (hasRecommendedPrices(order) && hasVat(order)){
			Amount net = AmountCalculator.calculateNetAmount(order);
			Amount vat = AmountCalculator.calculateVatAmount(order, order.getVatRate());
			builder.addFooterRow("Warenwert netto:   " + net.toString())
			.addFooterRow("zzgl. 19% MwSt.   " + vat.toString())
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
