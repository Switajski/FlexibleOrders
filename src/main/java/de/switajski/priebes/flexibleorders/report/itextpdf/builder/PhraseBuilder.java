package de.switajski.priebes.flexibleorders.report.itextpdf.builder;

import java.io.UnsupportedEncodingException;

import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;

import de.switajski.priebes.flexibleorders.report.itextpdf.PriebesIText5PdfView;

public class PhraseBuilder {
	
	private String text;
	private Font font = PriebesIText5PdfView.font;
	
	public PhraseBuilder(String text) {
		this.text = text;
	}

	public Phrase build(){
		Phrase p;
		try {
			p = new Phrase(new String(text.getBytes("UTF-8")), font);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return p;
	}
	
	public PhraseBuilder withFont(Font font){
		this.font = font;
		return this;
	}
	
	public PhraseBuilder withText(String text){
		this.text = text;
		return this;
	}
	
	public static PhraseBuilder bold(String text){
		return new PhraseBuilder(text).withFont(PriebesIText5PdfView.boldFont);
	}
	
	public static PhraseBuilder size11(String text){
		return new PhraseBuilder(text).withFont(PriebesIText5PdfView.twelveSizefont);
	}
	
}
