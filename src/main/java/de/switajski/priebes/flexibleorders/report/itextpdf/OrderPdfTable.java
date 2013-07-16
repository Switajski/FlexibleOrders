package de.switajski.priebes.flexibleorders.report.itextpdf;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.report.Order;

public class OrderPdfTable extends ItemPdfTable<OrderItem> {
	
	private Order bestellung;
	
	public OrderPdfTable(Order bestellung) {
		super(bestellung.getItems());
		this.bestellung = bestellung;
		createFooter();
	}

	@Override
	protected void createFooter() {
		PdfPCell warenwertNetto = new PdfPCell(new Phrase(
				"Warenwert Netto:   "
						+ decimalFormat.format(bestellung.getNetAmount())
				));
		warenwertNetto.setBorder(Rectangle.TOP);
		warenwertNetto.setColspan(6);
		warenwertNetto.setHorizontalAlignment(Element.ALIGN_RIGHT);
		this.addCell(warenwertNetto);
		
		PdfPCell steuer = new PdfPCell(new Phrase(
				"zzgl. 19% MwSt.   "+ decimalFormat.format(bestellung.getTax())));
		steuer.setColspan(6);
		steuer.setHorizontalAlignment(Element.ALIGN_RIGHT);
		steuer.setBorder(Rectangle.NO_BORDER);
		this.addCell(steuer);
		
		Phrase betragBruttoPhrase = new Phrase(
				"Gesamtbetrag Brutto:   "
					+ decimalFormat.format(bestellung.getGrossAmount()));
		betragBruttoPhrase.setFont(FontFactory.getFont(PriebesIText5PdfView.FONT,10,Font.BOLD));
		PdfPCell betragBrutto = new PdfPCell(betragBruttoPhrase);
		betragBrutto.setColspan(6);
		betragBrutto.setHorizontalAlignment(Element.ALIGN_RIGHT);
		betragBrutto.setBorder(Rectangle.NO_BORDER);
		this.addCell(betragBrutto);
		
	}

}
