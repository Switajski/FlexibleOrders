package de.switajski.priebes.flexibleorders.itextpdf.template;

import java.io.IOException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;

public class PageWriter implements PdfPageEvent {

    PdfTemplate total;
    private int pageNumber = 1;

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 16);
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        addPageNumber(writer, document);
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
        absText(
                writer,
                String.format("S. " + pageNumber + " / ", writer.getPageNumber()),
                x - 25,
                y + 2);

    }

    private static void absText(PdfWriter writer, String text, int x, int y) {
        PdfContentByte cb = writer.getDirectContent();
        try {
            BaseFont bf = BaseFont.createFont(
                    BusinessLetterPdfTemplate.FONT,
                    BusinessLetterPdfTemplate.ENCODING,
                    BaseFont.NOT_EMBEDDED);
            cb.saveState();
            cb.beginText();
            cb.moveText(x, y);
            cb.setFontAndSize(bf, BusinessLetterPdfTemplate.FONT_SIZE);
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
    public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {}

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

}
