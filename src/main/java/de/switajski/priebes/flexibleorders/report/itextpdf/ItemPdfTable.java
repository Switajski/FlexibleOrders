package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.Item;

public abstract class ItemPdfTable<T extends Item> extends PdfPTable{

	protected final static int COLUMNS = 4;
	public ArrayList<T> bpList;
	public final static DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
	private static final float TOTAL_WIDTH = 500;
	
	public ItemPdfTable(List<T> bestellpositionen) {
		super(COLUMNS);
		try {
			this.setWidths(new int[]{100, 245, 90, 65});
		} catch (DocumentException e) {
			e.printStackTrace();
		}        
		this.setTotalWidth(TOTAL_WIDTH);
		this.setLockedWidth(true);
//		this.getDefaultCell().setFixedHeight(20);
		bpList = getSortedItemen(bestellpositionen);
		
		createHeader();
		createBody();
	}
	
	private void createHeader() {
		ArrayList<PdfPCell> header = new ArrayList<PdfPCell>();
		PdfPCell bposHeader = new PdfPCell(new Phrase("Bestellpos."));
		bposHeader.setFixedHeight(25f);
		bposHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
		header.add(bposHeader);

		PdfPCell artikelHeader = new PdfPCell(new Phrase("Artikel"));
		artikelHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
		header.add(artikelHeader);

		PdfPCell mengeHeader = new PdfPCell(new Phrase("Menge x Preis"));
		mengeHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
		header.add(mengeHeader);

		PdfPCell betragHeader = new PdfPCell(new Phrase("Betrag"));
		betragHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
		header.add(betragHeader);

		for (PdfPCell cell:header){
			cell.setBorder(Rectangle.BOTTOM);
			this.addCell(cell);
		}
	}
	
	private void createBody() {
		
		
		for (T bp:bpList){
			ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();

			PdfPCell bestellpos = new PdfPCell(new Phrase(getItemNumber(bp)));
			bestellpos.setFixedHeight(20f);
			cells.add(bestellpos);
			
			PdfPCell artikelname = new PdfPCell(new Phrase(
					"Art.Nr.: "
					+ bp.getProductNumber() 
					+ " - "
					+ bp.getProductName()
					));
			artikelname.setHorizontalAlignment(Element.ALIGN_LEFT);
			cells.add(artikelname);

			PdfPCell preis = new PdfPCell(new Phrase(
					bp.getQuantity()
					+ " x "
					+ decimalFormat.format(bp.getPriceNet())
					+ " Ä"
					));
			preis.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cells.add(preis);

			BigDecimal betragValue = bp.getPriceNet().multiply(new BigDecimal(bp.getQuantity()));
			PdfPCell betrag = new PdfPCell(new Phrase(
					decimalFormat.format(betragValue)
					+ " Ä"
					));
			betrag.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cells.add(betrag);

			for (PdfPCell cell:cells){
				cell.setBorder(Rectangle.NO_BORDER);
				this.addCell(cell);
			}
		}

	}


	protected String getItemNumber(T bp) {
		return String.valueOf(
				bp.getOrderNumber());
	}

	/**
	 * Der Footer der Itemtabelle ist von Dokument (Rechnung,
	 * Auftragsbest√§tigung, ...) anders. Bspw. kommen Versandkosten hinzu.
	 */
	protected abstract void createFooter();

	private ArrayList<T> getSortedItemen(
			Collection<T> bestellpositionen) {
		
		ArrayList<T> bps = new ArrayList<T>();
		for (T bp:bestellpositionen){
			bps.add(bp);
		}
		Collections.sort(bps);
		return bps;
	}
	

}
