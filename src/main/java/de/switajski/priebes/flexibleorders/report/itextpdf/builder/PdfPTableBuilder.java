package de.switajski.priebes.flexibleorders.report.itextpdf.builder;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.report.itextpdf.PriebesIText5PdfView;

public class PdfPTableBuilder {
	
	private int COLUMNS = 4;
	int[] widths = new int[]{100, 245, 90, 65};
	private static int align1 = Element.ALIGN_LEFT;
	private static int align2 = Element.ALIGN_LEFT;
	private static int align3 = Element.ALIGN_RIGHT;
	private static int align4 = Element.ALIGN_RIGHT;

	private List<FourStrings> bodyList = new ArrayList<FourStrings>();
	private List<String> footerList = new ArrayList<String>();
	private float totalWidth = 500;
	private FourStrings headerStrings;
	
	private String firstColumn = null;
	//FIXME add page handling (Builder pattern won't work here)
	private Document doc;
	
	public PdfPTableBuilder(Document doc) {
		this.doc = doc;
	}

	public PdfPTable build(){
		PdfPTable pdfPTable = new PdfPTable(COLUMNS);
		pdfPTable = new PdfPTable(COLUMNS);
		try {
			pdfPTable.setWidths(widths);
		} catch (DocumentException e) {
			e.printStackTrace();
		}        
		pdfPTable.setTotalWidth(totalWidth);
		pdfPTable.setLockedWidth(true);
//		this.getDefaultCell().setFixedHeight(20);
		
		createHeader(pdfPTable, headerStrings);
		createBody(pdfPTable);
		createFooter(pdfPTable);
		return pdfPTable;
	}
	
	private void createFooter(PdfPTable pdfPTable) {
		int current = 0;
		int last = footerList.size()-1;
		for (String string: footerList){
			
			Phrase phrase = new Phrase(string);
			if (current == last)
				phrase.setFont(FontFactory.getFont(PriebesIText5PdfView.FONT,10,Font.BOLD));
			PdfPCellBuilder builder = new PdfPCellBuilder(phrase)
				.setColspan(6)
				.setRightHorizontalAlignment();
			if (current == 0) builder.setTopBorder();
			pdfPTable.addCell(builder.build());
			current++;
		
		}
	}

	private void createBody(PdfPTable pdfPTable) {
		int bPos = 1;
		for (FourStrings bp:bodyList){
			ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
			
			String firstColumnContent = null;
			// if getFirstColumnContent is not overridden by subclasses enumerate positions
			if (firstColumn==null) 
				firstColumnContent = String.valueOf(bPos);
			else 
				firstColumnContent = firstColumn;
			
			// item position
			PdfPCell bestellpos = PdfPCellBuilder.leftAligned(firstColumnContent);
			bestellpos.setFixedHeight(20f);
			cells.add(bestellpos);

			cells.add(PdfPCellBuilder.leftAligned(bp.second));

			cells.add(PdfPCellBuilder.rightAligned(bp.third));

			cells.add(PdfPCellBuilder.rightAligned(bp.forth));

			for (PdfPCell cell:cells){
				cell.setBorder(Rectangle.NO_BORDER);
				pdfPTable.addCell(cell);
			}
			bPos++;
			
		}
		
	}
	
	private void createHeader(PdfPTable pdfPTable, FourStrings strings) {
		ArrayList<PdfPCell> header = new ArrayList<PdfPCell>();
		PdfPCell bposHeader = new PdfPCell(new Phrase(strings.first));
		bposHeader.setFixedHeight(25f);
		bposHeader.setHorizontalAlignment(align1);
		header.add(bposHeader);

		PdfPCell artikelHeader = new PdfPCell(new Phrase(strings.second));
		artikelHeader.setHorizontalAlignment(align2);
		header.add(artikelHeader);

		PdfPCell mengeHeader = new PdfPCell(new Phrase(strings.third));
		mengeHeader.setHorizontalAlignment(align3);
		header.add(mengeHeader);

		PdfPCell betragHeader = new PdfPCell(new Phrase(strings.forth));
		betragHeader.setHorizontalAlignment(align4);
		header.add(betragHeader);

		for (PdfPCell cell:header){
			cell.setBorder(Rectangle.BOTTOM);
			pdfPTable.addCell(cell);
		}
	}

	public PdfPTableBuilder addBodyRow(FourStrings row){
		this.bodyList.add(row);
		return this;
	}

	public PdfPTableBuilder setHeader(FourStrings header) {
		this.headerStrings = header;
		return this;
	}

	public PdfPTableBuilder addFooterRow(String string) {
		this.footerList.add(string);
		return this;
	}

}
