package de.switajski.priebes.flexibleorders.itextpdf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPCellBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;

public class PdfUtils {

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static final int PAGE_MARGIN_BOTTOM = /* bottom */190;
    public static final int PAGE_MARGIN_TOP = /* top */80;
    public static final int PAGE_MARGIN_RIGHT = /* right */72;
    public static final int PAGE_MARGIN_LEFT = /* left */60;

    public boolean hasPackageNumbers(ReportDto cReport) {
        boolean hasPackageNumbers = false;
        for (ReportItem ri : cReport.items) {
            if (ri instanceof ShippingItem) {
                ShippingItem ci = (ShippingItem) ri;
                if (ci.getPackageNumber() != null) hasPackageNumbers = true;
            }
        }
        return hasPackageNumbers;
    }

    public void addPaymentConditions(String paymentConditions,
            CustomPdfPTableBuilder footerBuilder) {
        PhraseBuilder bold = new PhraseBuilder("").withFont(FontFactory
                .getFont(PriebesIText5PdfView.FONT,
                        PriebesIText5PdfView.FONT_SIZE,
                        Font.BOLD));
        footerBuilder.addCell(new PdfPCellBuilder(bold.build())
                .withPhrase(new PhraseBuilder().withText("Zahlungskonditionen: " + paymentConditions).size8().build())
                .withBorder(Rectangle.NO_BORDER)
                .withColSpan(2)
                .build());
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * sets format and margins of document
     *
     * @return
     */
    public Document newDocument() {
        Document doc = new Document(PageSize.A4);
        doc.setMargins(
                PAGE_MARGIN_LEFT,
                PAGE_MARGIN_RIGHT,
                PAGE_MARGIN_TOP,
                PAGE_MARGIN_BOTTOM);
        return doc;
    }

}
