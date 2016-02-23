package de.switajski.priebes.flexibleorders.itextpdf.template;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;

public class FooterWriter implements PdfPageEvent {

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {

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
                        .withFont(BusinessLetterPdfTemplate.eightSizeFont)
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
