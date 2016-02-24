package de.switajski.priebes.flexibleorders.itextpdf.template;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.ReportViewHelper;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;

public class DateOfDocumentWriter implements PdfPageEvent {

    private String date;
    private boolean eachPage;
    private String noteOnDate;

    public DateOfDocumentWriter(String date, String noteOnDate, boolean eachPage) {
        this.date = date;
        this.eachPage = eachPage;
        this.noteOnDate = noteOnDate;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        if (!eachPage) addDateOfDocument(document);
    }

    private void addDateOfDocument(Document document) {
        try {
            document.add(ReportViewHelper.createDate(date));
            if (!StringUtils.isEmpty(noteOnDate)) document.add(new ParagraphBuilder(noteOnDate)
                    .withFont(BusinessLetterPdfTemplate.eightSizeFont)
                    .withAlignment(Element.ALIGN_RIGHT)
                    .build());
        }
        catch (DocumentException e) {
            throw new ExceptionConverter(e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        if (eachPage) addDateOfDocument(document);
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
