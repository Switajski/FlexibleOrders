package de.switajski.priebes.flexibleorders.itextpdf.builder;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import de.switajski.priebes.flexibleorders.web.itextpdf.PriebesIText5PdfView;

public class ColumnFormat {

	public String heading;
	public int alignment;
	public float relativeWidth;
	public Font font;

	public ColumnFormat(String heading, int alignment, float relativeWidth) {
		this.heading = heading;
		this.alignment = alignment;
		this.relativeWidth = relativeWidth;
	}
	
	public ColumnFormat(String heading, int alignment, float relativeWidth, Font font) {
		this.heading = heading;
		this.alignment = alignment;
		this.relativeWidth = relativeWidth;
		this.font = font;
	}

	public PdfPCell createCell(String content) {
		PhraseBuilder phraseB = new PhraseBuilder(content);
		if (font == null)
			phraseB.withFont(PriebesIText5PdfView.font);
		else 
			phraseB.withFont(font);
		Phrase phrase = phraseB.build();
		
		PdfPCellBuilder pdfPCellBuilder = new PdfPCellBuilder(phrase);
		
		if (alignment == Element.ALIGN_LEFT)
			pdfPCellBuilder.withLeftHorizontalAlignment();
		else
			pdfPCellBuilder.withRightHorizontalAlignment();
		
		return pdfPCellBuilder.build();
	}
}