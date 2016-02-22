package de.switajski.priebes.flexibleorders.itextpdf.template;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.PdfConfiguration;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;

public class LogoWriter implements PdfPageEvent {

    private Image logo;

    public LogoWriter(Image logo) {
        this.logo = logo;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        try {
            if (logo == null) logo = new PdfConfiguration().logo();
            Image img = logo;
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
                                    FontFactory.getFont(BusinessLetterPdfTemplate.FONT, 12, Font.NORMAL))
                            .withAlignment(Element.ALIGN_RIGHT)
                            .withLineSpacing(25f)
                            .build());
            headerCell.addElement(new ParagraphBuilder(BusinessLetterPdfTemplate.HEADER_ZEILE1)
                    .withAlignment(Element.ALIGN_RIGHT)
                    .build());
            headerCell.addElement(new ParagraphBuilder(BusinessLetterPdfTemplate.HEADER_ZEILE2)
                    .withAlignment(Element.ALIGN_RIGHT)
                    .build());
            headerCell.addElement(new ParagraphBuilder(BusinessLetterPdfTemplate.HEADER_ZEILE3)
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

    @Override
    public void onStartPage(PdfWriter writer, Document document) {

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
