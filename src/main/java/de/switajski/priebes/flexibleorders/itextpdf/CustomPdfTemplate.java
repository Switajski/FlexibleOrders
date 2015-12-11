package de.switajski.priebes.flexibleorders.itextpdf;

import java.io.IOException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;

public class CustomPdfTemplate implements PdfPageEvent {

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
            PriebesIText5PdfView.FONT,
            8,
            Font.NORMAL);
    public static Font eightSizeBoldFont = FontFactory.getFont(
            PriebesIText5PdfView.FONT,
            8,
            Font.BOLD);

    PdfTemplate total;
    private int pageNumber = 1;
    private Image logo;

    public CustomPdfTemplate(Image logo) {
        this.logo = logo;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 16);
        insertBigLogo(writer);
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        PdfPTable footer = new PdfPTable(1);
        footer.setTotalWidth(527);
        footer.setLockedWidth(true);
        footer.setHorizontalAlignment(Element.ALIGN_CENTER);
        footer.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        Paragraph fPara = new ParagraphBuilder(
                "priebes OHG / Maxstrasse 1 / 71636 Ludwigsburg")
                .addTextLine(
                        "www.priebes.eu / info@priebes.eu / 0176 41557068 / 07141 9475640 / Fax: 07141 6421002 ")
                .addTextLine(
                        "KSK Ludwigsburg BLZ 60450050 - Kto 30055142 / HRA 725747 / Ust-IdNr.: DE275948390")
                .addTextLine(
                        "IBAN: DE79604500500030055142 / BIC-/SWIFT-Code: SOLADES1LBG")
                .withAlignment(Element.ALIGN_CENTER)
                .withFont(eightSizeFont)
                .withLineSpacing(11f)
                .build();
        PdfPCell footerCell = new PdfPCell();
        footerCell.addElement(fPara);
        // footerCell.setBorder(Rectangle.TOP);
        footerCell.setBorder(Rectangle.NO_BORDER);
        footer.addCell(footerCell);
        footer.writeSelectedRows(
                0,
                -1, /* xPos */
                40, /* yPos */
                90,
                writer.getDirectContent());

        if (SHOW_PAGE_NUMBERS) addPageNumber(writer, document);

    }

    protected void addPageNumber(PdfWriter writer, Document document) {
        Image img2;
        int x = 550;
        int y = 20;
        try {
            img2 = Image.getInstance(total);
            img2.setAbsolutePosition(x, y);
            document.add(img2);
        }
        catch (BadElementException e) {
            e.printStackTrace();
        }
        catch (DocumentException e) {
            e.printStackTrace();
        }
        absText(writer, String.format("S. " + pageNumber + " / ", writer.getPageNumber()),
                x - 25, y + 2);

    }

    private static void absText(PdfWriter writer, String text, int x, int y) {
        PdfContentByte cb = writer.getDirectContent();
        try {
            BaseFont bf = BaseFont.createFont(PriebesIText5PdfView.FONT,
                    ENCODING, BaseFont.NOT_EMBEDDED);
            cb.saveState();
            cb.beginText();
            cb.moveText(x, y);
            cb.setFontAndSize(bf, PriebesIText5PdfView.FONT_SIZE);
            cb.showText(text);
            cb.endText();
            cb.restoreState();
        }
        catch (DocumentException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        pageNumber++;
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        if ((writer.getPageNumber() - 1) > 1) ColumnText
                .showTextAligned(
                        total,
                        Element.ALIGN_LEFT,
                        new PhraseBuilder(String.valueOf(writer
                                .getPageNumber() - 1)).build(),
                        2,
                        2,
                        0);
    }

    @Override
    public void onParagraph(PdfWriter writer, Document document, float paragraphPosition) {

    }

    @Override
    public void onParagraphEnd(PdfWriter writer, Document document, float paragraphPosition) {

    }

    @Override
    public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {

    }

    @Override
    public void onChapterEnd(PdfWriter writer, Document document, float paragraphPosition) {

    }

    @Override
    public void onSection(PdfWriter writer, Document document, float paragraphPosition, int depth, Paragraph title) {

    }

    @Override
    public void onSectionEnd(PdfWriter writer, Document document, float paragraphPosition) {

    }

    @Override
    public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {

    }

    public void insertBigLogo(PdfWriter writer) {
        try {
            Image img = Image.getInstance(logo);
            img.setAlignment(Image.RIGHT);
            img.scaleToFit(180, 75);

            PdfPTable table = new PdfPTable(3);
            // Adresse im Header
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.addElement(img);
            headerCell.addElement(
                    new ParagraphBuilder("priebes OHG")
                            .withFont(
                                    FontFactory.getFont(FONT, 12, Font.NORMAL))
                            .withAlignment(Element.ALIGN_RIGHT)
                            .withLineSpacing(25f)
                            .build());
            headerCell.addElement(new ParagraphBuilder(HEADER_ZEILE1)
                    .withAlignment(Element.ALIGN_RIGHT)
                    .build());
            headerCell.addElement(new ParagraphBuilder(HEADER_ZEILE2)
                    .withAlignment(Element.ALIGN_RIGHT)
                    .build());
            headerCell.addElement(new ParagraphBuilder(HEADER_ZEILE3)
                    .withAlignment(Element.ALIGN_RIGHT)
                    .build());

            table.setWidths(new int[] { 10, 10, 30 });
            table.setTotalWidth(527);
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(20);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell("");
            table.addCell("");
            table.addCell(headerCell);
            table.writeSelectedRows(0, -1, 0, 788, writer.getDirectContent());

        }
        catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

}
