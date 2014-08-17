package de.switajski.priebes.flexibleorders.itextpdf.builder;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;

import de.switajski.priebes.flexibleorders.web.itextpdf.PriebesIText5PdfView;

public class PhraseBuilder {

	private StringBuilder text = new StringBuilder();
	private Font font = PriebesIText5PdfView.font;

	public PhraseBuilder(String text) {
		this.text = new StringBuilder(text);
	}

	public PhraseBuilder() {
	}

	public Phrase build() {
		Phrase p;
		p = new Phrase(text.toString(), font);
		return p;
	}

	public PhraseBuilder withFont(Font font) {
		this.font = font;
		return this;
	}

	public PhraseBuilder withText(String text) {
		this.text = new StringBuilder(text);
		return this;
	}
	
	public PhraseBuilder append(String text){
		this.text.append(text);
		return this;
	}
	
	public PhraseBuilder append(Chunk text){
		this.text.append(text);
		return this;
	}

	public PhraseBuilder bold() {
		return withFont(PriebesIText5PdfView.boldFont);
	}

	public PhraseBuilder size11() {
		return withFont(PriebesIText5PdfView.twelveSizefont);
	}
	
	public PhraseBuilder size8(){
		return withFont(PriebesIText5PdfView.eightSizeFont);
	}

}
