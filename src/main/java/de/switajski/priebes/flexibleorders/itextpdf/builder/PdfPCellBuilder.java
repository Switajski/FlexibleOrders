package de.switajski.priebes.flexibleorders.itextpdf.builder;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;

import de.switajski.priebes.flexibleorders.web.itextpdf.PriebesIText5PdfView;

public class PdfPCellBuilder {

	private Phrase phrase;
	// for viewing borders of all tables:
	// private int border = Rectangle.BOX;
	private int border = Rectangle.NO_BORDER;
	private int colspan;
	private int horizontalAlignment;
	private int colSpan;

	public PdfPCellBuilder(Phrase phrase) {
		this.phrase = phrase;
	}

	public PdfPCell build() {
		PdfPCell cell = new PdfPCell(phrase);
		cell.setBorder(border);
		cell.setBorderWidth(PriebesIText5PdfView.BORDER_WIDTH);
		cell.setColspan(colspan);
		cell.setHorizontalAlignment(horizontalAlignment);
		cell.setColspan(colSpan);
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

	public PdfPCellBuilder withRightHorizontalAlignment() {
		this.horizontalAlignment = Element.ALIGN_RIGHT;
		return this;
	}

	public PdfPCellBuilder withLeftHorizontalAlignment() {
		this.horizontalAlignment = Element.ALIGN_LEFT;
		return this;
	}

	public static PdfPCell withLeftAlignment(String content) {
		return new PdfPCellBuilder(new Phrase(
				content,
				PriebesIText5PdfView.font))
				.withLeftHorizontalAlignment()
				.build();
	}

	public static PdfPCell withRightAlignment(String content) {
		return new PdfPCellBuilder(new Phrase(
				content,
				PriebesIText5PdfView.font))
				.withRightHorizontalAlignment()
				.build();
	}

	public PdfPCellBuilder withPhrase(Phrase phrase) {
		this.phrase = phrase;
		return this;
	}

	public PdfPCellBuilder withBorder(int rectangle) {
		this.border = rectangle;
		return this;
	}

    public PdfPCellBuilder withColSpan(int colSpan) {
        this.colSpan = colSpan;
        return this;
    }
}
