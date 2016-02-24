package de.switajski.priebes.flexibleorders.itextpdf.template;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.ReportViewHelper;

public class SubjectWriter implements PdfPageEvent {

    private String subject;
    private boolean eachPage;

    public SubjectWriter(String subject, boolean eachPage) {
        this.subject = subject;
        this.eachPage = eachPage;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        if (!eachPage) addSubject(document);
    }

    private void addSubject(Document document) {
        try {
            for (Paragraph p : ReportViewHelper.createHeading(subject)) {
                document.add(p);
            }
        }
        catch (DocumentException e) {
            throw new ExceptionConverter(e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        if (eachPage) addSubject(document);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {

    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {

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

}
