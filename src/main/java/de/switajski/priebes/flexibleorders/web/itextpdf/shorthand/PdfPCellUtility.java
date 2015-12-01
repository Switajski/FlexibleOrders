package de.switajski.priebes.flexibleorders.web.itextpdf.shorthand;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;
import de.switajski.priebes.flexibleorders.web.itextpdf.PriebesIText5PdfView;

public class PdfPCellUtility {

    public static PdfPCell noBorder(PdfPCell cell) {
        cell.setBorder(0);
        return cell;
    }

    public static PdfPCell noBorder() {
        PdfPCell cell = new PdfPCell();
        noBorder(cell);
        return cell;
    }

    public static PdfPCell cellWithSmallFont(String createString) {
        return wrapInCell(new PhraseBuilder(createString).size8().build());
    }

    public static PdfPCell cellWithSmallBoldFont(String text) {
        return wrapInCell(new PhraseBuilder(text).size8Bold().build());
    }

    public static PdfPCell cellWithMixedFont(String bold, String normal) {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk(bold, PriebesIText5PdfView.eightSizeBoldFont));
        paragraph.add(new Chunk(normal, PriebesIText5PdfView.eightSizeFont));
        return wrapInCell(paragraph);
    }

    public static PdfPCell wrapInCell(Phrase build) {
        PdfPCell cell = new PdfPCell(build);
        noBorder(cell);
        return cell;
    }

    public static PdfPCell emptyCell() {
        return wrapInCell(new PhraseBuilder(" ").build());
    }

}
