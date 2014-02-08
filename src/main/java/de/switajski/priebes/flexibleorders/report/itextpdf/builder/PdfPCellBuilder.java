package de.switajski.priebes.flexibleorders.report.itextpdf.builder;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;

public class PdfPCellBuilder {
	
	private Phrase phrase;

	private int border = Rectangle.NO_BORDER;
	private int colspan;
	private int horizontalAlignment;
	
	public PdfPCellBuilder(Phrase phrase) {
		this.phrase = phrase;
	}
	
	public PdfPCell build(){
		PdfPCell cell = new PdfPCell(phrase);
		cell.setBorder(border);
		cell.setColspan(colspan);
		cell.setHorizontalAlignment(horizontalAlignment);
		return cell;
	}
	
	public PdfPCellBuilder setTopBorder() {
		border = Rectangle.TOP;
		return this;
	}
	public PdfPCellBuilder setColspan(int colspan) {
		this.colspan = colspan;
		return this;
	}
	public PdfPCellBuilder setRightHorizontalAlignment() {
		this.horizontalAlignment = Element.ALIGN_RIGHT;
		return this;
	}
	public PdfPCellBuilder setLeftHorizontalAlignment() {
		this.horizontalAlignment = Element.ALIGN_LEFT;
		return this;
	}
	
	public static PdfPCell leftAligned(String content){
		return new PdfPCellBuilder(new Phrase(content))
		.setLeftHorizontalAlignment()
		.build();
	}
	public static PdfPCell rightAligned(String content){
		return new PdfPCellBuilder(new Phrase(content))
		.setRightHorizontalAlignment()
		.build();
	}
}
