package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.util.List;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;

import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;

public class OrderConfirmationPdfTable extends ItemPdfTable<ShippingItem> {
	
	private OrderConfirmation orderConfirmation;
	
	public OrderConfirmationPdfTable(
			OrderConfirmation orderConfirmation2,
			List<ShippingItem> list) {
		super(list);
		this.orderConfirmation = orderConfirmation2;
	}

	@Override
	protected void createFooter() {
		PdfPCell warenwertNetto = new PdfPCell(new Phrase(
				"Warenwert netto:   "
						+ DECIMAL_FORMAT.format(orderConfirmation.getGrossAmount())
				));
		warenwertNetto.setBorder(Rectangle.TOP);
		warenwertNetto.setColspan(6);
		warenwertNetto.setHorizontalAlignment(Element.ALIGN_RIGHT);
		getTable().addCell(warenwertNetto);
		
		//TODO: implement expected shipping costs
		/*PdfPCell versandNetto = new PdfPCell(new Phrase(
				"Versand netto   "+ decimalFormat.format(orderConfirmation.get())));
		versandNetto.setColspan(6);
		versandNetto.setHorizontalAlignment(Element.ALIGN_RIGHT);
		versandNetto.setBorder(Rectangle.NO_BORDER);
		this.addCell(versandNetto);*/
		
		
		PdfPCell steuer = new PdfPCell(new Phrase(
				"zzgl. 19% MwSt.   "+ DECIMAL_FORMAT.format(orderConfirmation.getTax())));
		steuer.setColspan(6);
		steuer.setHorizontalAlignment(Element.ALIGN_RIGHT);
		steuer.setBorder(Rectangle.NO_BORDER);
		getTable().addCell(steuer);
		
		Phrase betragBruttoPhrase = new Phrase(
				"Gesamtbetrag brutto:   "
					+ DECIMAL_FORMAT.format(orderConfirmation.getGrossAmount()));
		betragBruttoPhrase.setFont(FontFactory.getFont(PriebesIText5PdfView.FONT,10,Font.BOLD));
		PdfPCell betragBrutto = new PdfPCell(betragBruttoPhrase);
		betragBrutto.setColspan(6);
		betragBrutto.setHorizontalAlignment(Element.ALIGN_RIGHT);
		betragBrutto.setBorder(Rectangle.NO_BORDER);
		getTable().addCell(betragBrutto);
		
	}
	
	@Override
	public String getFirstColumnContent(Item item) {
		return item.getOrderNumber().toString();
	}

}
