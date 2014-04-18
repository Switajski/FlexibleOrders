package de.switajski.priebes.flexibleorders.itextpdf.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

import de.switajski.priebes.flexibleorders.web.itextpdf.PriebesIText5PdfView;

/**
 * Builder to ease creation of IText's {@link Paragraph}
 * 
 * @author Marek Switajski
 * 
 */
public class ParagraphBuilder {

	private Font font = PriebesIText5PdfView.font;
	private int alignment = Element.ALIGN_LEFT;
	private List<String> texts = new ArrayList<String>();
	private float lineSpacing = 13f;
	private float indentationLeft = 0f;
	private float indentationRight = 0f;

	public ParagraphBuilder(String text) {
		this.texts.add(text);
	}

	public ParagraphBuilder(List<String> texts) {
		this.texts = texts;
	}

	public Paragraph build() {
		Paragraph p = new Paragraph(lineSpacing);
		p.setFont(font);
		p.setAlignment(alignment);
		if (indentationLeft != 0f)
			p.setIndentationLeft(indentationLeft);
		if (indentationRight != 0f)
			p.setIndentationRight(indentationRight);
		ListIterator<String> itr = texts.listIterator();
		while (itr.hasNext()) {
			p.add(itr.next());
			if (itr.hasNext())
				p.add(Chunk.NEWLINE);
		}
		return p;
	}

	/**
	 * 
	 * @param {@link Element} alignment
	 * @return
	 */
	public ParagraphBuilder withAlignment(int alignment) {
		this.alignment = alignment;
		return this;
	}

	public ParagraphBuilder addTextLine(String text) {
		this.texts.add(text);
		return this;
	}

	public ParagraphBuilder withFont(Font font) {
		this.font = font;
		return this;
	}

	public ParagraphBuilder withLineSpacing(float lineSpacing) {
		this.lineSpacing = lineSpacing;
		return this;
	}

	public static Paragraph createEmptyLine() {
		Paragraph p = new Paragraph();
		p.add(Chunk.NEWLINE);
		return p;
	}

	public ParagraphBuilder withIndentationRight(float indent) {
		this.indentationRight = indent;
		return this;
	}

	public ParagraphBuilder withIndentationLeft(float indentationLeft) {
		this.indentationLeft = indentationLeft;
		return this;
	}

}
