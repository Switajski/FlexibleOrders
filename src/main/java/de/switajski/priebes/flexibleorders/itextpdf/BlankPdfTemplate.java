package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;

public class BlankPdfTemplate implements PdfPageEvent {

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onParagraph(PdfWriter writer, Document document, float paragraphPosition) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onParagraphEnd(PdfWriter writer, Document document, float paragraphPosition) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChapterEnd(PdfWriter writer, Document document, float paragraphPosition) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSection(PdfWriter writer, Document document, float paragraphPosition, int depth, Paragraph title) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSectionEnd(PdfWriter writer, Document document, float paragraphPosition) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
        // TODO Auto-generated method stub

    }

}
