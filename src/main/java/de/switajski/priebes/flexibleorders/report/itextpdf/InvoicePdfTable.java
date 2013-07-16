package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.util.List;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;

import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.report.Invoice;

public class InvoicePdfTable extends ItemPdfTable<InvoiceItem> {
	
	private Invoice invoice;
	
	public InvoicePdfTable(
			Invoice invoice,
			List<InvoiceItem> abPositions) {
		super(abPositions);
		this.invoice = invoice;
		createFooter();
	}

	@Override
	protected void createFooter() {
		PdfPCell warenwertNetto = new PdfPCell(new Phrase(
				"Warenwert netto:   "
						+ decimalFormat.format(invoice.getNetAmount())
				));
		warenwertNetto.setBorder(Rectangle.TOP);
		warenwertNetto.setColspan(6);
		warenwertNetto.setHorizontalAlignment(Element.ALIGN_RIGHT);
		this.addCell(warenwertNetto);
		
		/*PdfPCell versandNetto = new PdfPCell(new Phrase(
				"Versand netto   "
						+ decimalFormat.format(invoice.getVersandNetto())));
		versandNetto.setColspan(6);
		versandNetto.setHorizontalAlignment(Element.ALIGN_RIGHT);
		versandNetto.setBorder(Rectangle.NO_BORDER);
		this.addCell(versandNetto);*/
		
		
		PdfPCell steuer = new PdfPCell(new Phrase(
				"zzgl. "+invoice.getTaxRate()+"% MwSt.   "
						+ decimalFormat.format(invoice.getTax())));
		steuer.setColspan(6);
		steuer.setHorizontalAlignment(Element.ALIGN_RIGHT);
		steuer.setBorder(Rectangle.NO_BORDER);
		this.addCell(steuer);
		
		Phrase betragBruttoPhrase = new Phrase(
				"Gesamtbetrag brutto:   "
					+ decimalFormat.format(invoice.getGrossAmount()));
		betragBruttoPhrase.setFont(FontFactory.getFont(PriebesIText5PdfView.FONT,10,Font.BOLD));
		PdfPCell betragBrutto = new PdfPCell(betragBruttoPhrase);
		betragBrutto.setColspan(6);
		betragBrutto.setHorizontalAlignment(Element.ALIGN_RIGHT);
		betragBrutto.setBorder(Rectangle.NO_BORDER);
		this.addCell(betragBrutto);
		
	}

}
