package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public abstract class ItemPdfTable {

	protected final static int COLUMNS = 4;
	public ArrayList<ReportItem> bpList;
	public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat(
			",##0.00");
	public static final float TOTAL_WIDTH = 500;

	private String firstColumnName = "Bestellpos.";
	private String secondColumnName = "Artikel";
	private String thirdColumnName = "Menge x Preis";
	private String forthColumnName = "Betrag";

	private PdfPTable pdfPTable;

	public ItemPdfTable(Set<ReportItem> set) {
		pdfPTable = new PdfPTable(COLUMNS);
		try {
			pdfPTable.setWidths(new int[] { 100, 245, 90, 65 });
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		pdfPTable.setTotalWidth(TOTAL_WIDTH);
		pdfPTable.setLockedWidth(true);
		// this.getDefaultCell().setFixedHeight(20);
		bpList = getSortedItemen(set);

	}

	public PdfPTable build() {
		createHeader();
		createBody();
		createFooter();
		return pdfPTable;
	}

	/**
	 * creates the header of the table
	 */
	private void createHeader() {
		ArrayList<PdfPCell> header = new ArrayList<PdfPCell>();
		PdfPCell bposHeader = new PdfPCell(new Phrase(firstColumnName));
		bposHeader.setFixedHeight(25f);
		bposHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
		header.add(bposHeader);

		PdfPCell artikelHeader = new PdfPCell(new Phrase(secondColumnName));
		artikelHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
		header.add(artikelHeader);

		PdfPCell mengeHeader = new PdfPCell(new Phrase(thirdColumnName));
		mengeHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
		header.add(mengeHeader);

		PdfPCell betragHeader = new PdfPCell(new Phrase(forthColumnName));
		betragHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
		header.add(betragHeader);

		for (PdfPCell cell : header) {
			cell.setBorder(Rectangle.BOTTOM);
			pdfPTable.addCell(cell);
		}
	}

	/**
	 * create the body of the table
	 */
	private void createBody() {

		int bPos = 1;
		for (ReportItem bp : bpList) {
			ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();

			String firstColumnContent = null;
			// if getFirstColumnContent is not overriden by sublasses enumerate
			// positions
			if (getFirstColumnContent(bp) == null)
				firstColumnContent = String.valueOf(bPos);
			else
				firstColumnContent = getFirstColumnContent(bp);
			PdfPCell bestellpos = new PdfPCell(new Phrase(firstColumnContent));
			bestellpos.setFixedHeight(20f);
			cells.add(bestellpos);

			PdfPCell artikelname = new PdfPCell(new Phrase(
					"Art.Nr.: "
							+ bp.getOrderItem().getProduct().getProductNumber()
							+ " - "
							+ bp.getOrderItem().getProduct().getName()
					));
			artikelname.setHorizontalAlignment(Element.ALIGN_LEFT);
			cells.add(artikelname);

			PdfPCell preis = new PdfPCell(new Phrase(
					bp.getQuantity()
							+ " x "
							+ DECIMAL_FORMAT.format(bp
									.getOrderItem()
									.getNegotiatedPriceNet()
									.getValue())
							+ " Ä"
					));
			preis.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cells.add(preis);

			BigDecimal betragValue = bp
					.getOrderItem()
					.getNegotiatedPriceNet()
					.getValue()
					.multiply(new BigDecimal(bp.getQuantity()));
			PdfPCell betrag = new PdfPCell(new Phrase(
					DECIMAL_FORMAT.format(betragValue)
							+ " Ä"
					));
			betrag.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cells.add(betrag);

			for (PdfPCell cell : cells) {
				cell.setBorder(Rectangle.NO_BORDER);
				pdfPTable.addCell(cell);
			}
			bPos++;
		}

	}

	/**
	 * Method to override for invoice and archive
	 * 
	 * @return
	 */
	public String getFirstColumnContent(ReportItem item) {
		return null;
	}

	/**
	 * Der Footer der Itemtabelle ist von Dokument (Rechnung,
	 * Auftragsbest√§tigung, ...) anders. Bspw. kommen Versandkosten hinzu.
	 */
	protected abstract void createFooter();

	private ArrayList<ReportItem> getSortedItemen(
			Collection<ReportItem> bestellpositionen) {

		ArrayList<ReportItem> bps = new ArrayList<ReportItem>();
		for (ReportItem bp : bestellpositionen) {
			bps.add(bp);
		}
		Collections.sort(bps);
		return bps;
	}

	public String getFirstColumnName() {
		return firstColumnName;
	}

	public void setFirstColumnName(String firstColumnName) {
		this.firstColumnName = firstColumnName;
	}

	public String getSecondColumnName() {
		return secondColumnName;
	}

	public void setSecondColumnName(String secondColumnName) {
		this.secondColumnName = secondColumnName;
	}

	public String getThirdColumnName() {
		return thirdColumnName;
	}

	public void setThirdColumnName(String thirdColumnName) {
		this.thirdColumnName = thirdColumnName;
	}

	public String getForthColumnName() {
		return forthColumnName;
	}

	public void setForthColumnName(String forthColumnName) {
		this.forthColumnName = forthColumnName;
	}

	public PdfPTable getTable() {
		return pdfPTable;
	}

}
