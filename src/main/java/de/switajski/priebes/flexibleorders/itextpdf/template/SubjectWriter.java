package de.switajski.priebes.flexibleorders.itextpdf.template;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.builder.ColumnFormat;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.shorthand.PdfPCellUtility;

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
            PdfPTable table = new PdfPTableBuilder(leftAlignTableProperties()).withHeader(false).build();
            createHeading(subject).forEach((phrase) -> {
                table.addCell(PdfPCellUtility.wrapInCell(phrase));
            });
            document.add(table);
        }
        catch (DocumentException e) {
            throw new ExceptionConverter(e);
        }
    }

    private ArrayList<ColumnFormat> leftAlignTableProperties() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat("", Element.ALIGN_LEFT, 100));
        return rowProperties;
    }

    private List<Paragraph> createHeading(String heading)
            throws DocumentException {
        List<Paragraph> paragraphs = new ArrayList<Paragraph>();
        paragraphs.add(new ParagraphBuilder(heading).withFont(
                FontFactory.getFont(BusinessLetterPdfTemplate.FONT, 12, Font.BOLD))
                .build());
        paragraphs.add(ParagraphBuilder.createEmptyLine());

        return paragraphs;
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
