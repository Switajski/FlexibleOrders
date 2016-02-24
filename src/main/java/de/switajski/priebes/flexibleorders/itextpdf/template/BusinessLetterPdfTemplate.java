package de.switajski.priebes.flexibleorders.itextpdf.template;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.events.PdfPageEventForwarder;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;

public class BusinessLetterPdfTemplate extends PdfPageEventForwarder {

    protected static final String UEBER_EMPFAENGERADRESSE = "Maxstrasse1, 71636 Ludwigsburg";
    protected static final String HEADER_ZEILE1 = "Maxstrasse 1";
    protected static final String HEADER_ZEILE2 = "71636 Ludwigsburg";
    protected static final String HEADER_ZEILE3 = "www.priebes.eu";

    protected static final boolean SHOW_PAGE_NUMBERS = false;

    /**
     * font settings
     */
    public static final String ENCODING = BaseFont.CP1252;
    public static final String FONT = BaseFont.HELVETICA;
    public static final float FONT_SIZE = 10;
    public static final Font font = FontFactory.getFont(
            FONT,
            ENCODING,
            true,
            FONT_SIZE);
    public static final Font boldFont = FontFactory.getFont(
            FONT,
            ENCODING,
            true,
            FONT_SIZE + 2);
    public static final Font twelveSizefont = FontFactory.getFont(
            FONT,
            12,
            Font.BOLD);
    public static Font eightSizeFont = FontFactory.getFont(
            BusinessLetterPdfTemplate.FONT,
            8,
            Font.NORMAL);
    public static Font eightSizeBoldFont = FontFactory.getFont(
            BusinessLetterPdfTemplate.FONT,
            8,
            Font.BOLD);

    public static final float WIDTH = 464f;
    public static final int PAGE_MARGIN_BOTTOM = /* bottom */190;
    public static final int PAGE_MARGIN_TOP = /* top */80;
    public static final int PAGE_MARGIN_RIGHT = /* right */72;
    public static final int PAGE_MARGIN_LEFT = /* left */60;
    public static final int FOOTER_MARGIN_BOTTOM = 5;
    public static final float BORDER_WIDTH = 0.15f;

    public BusinessLetterPdfTemplate(Image logo, Address address, String date, String noteOnDate, String subject) {
        boolean eachPage = true;
        addPageEvent(new LogoWriter(logo, eachPage));
        addPageEvent(new AddressWriter(address, logo, eachPage));
        addPageEvent(new DateOfDocumentWriter(date, noteOnDate, eachPage));
        addPageEvent(new SubjectWriter(subject, eachPage));
        addPageEvent(new FooterWriter());
    }

}
