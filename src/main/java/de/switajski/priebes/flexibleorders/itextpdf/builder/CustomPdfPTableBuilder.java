package de.switajski.priebes.flexibleorders.itextpdf.builder;

import java.util.ArrayList;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.web.itextpdf.PriebesIText5PdfView;

//TODO make this default builder and let PdfPTableBuilder extend this 
public class CustomPdfPTableBuilder {

    public ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();

    private ArrayList<TableProperties> tableProperties;

    private Float totalWidth = null;

    public CustomPdfPTableBuilder(
            ArrayList<TableProperties> tableProperties) {
        this.tableProperties = tableProperties;
    }

    public PdfPTable build() {
        PdfPTable pdfPTable = new PdfPTable(tableProperties.size());
        for (PdfPCell cell : cells)
            pdfPTable.addCell(cell);
        if (totalWidth != null) pdfPTable.setTotalWidth(totalWidth);
        return pdfPTable;
    }

    public CustomPdfPTableBuilder addCell(PdfPCell cell) {
        this.cells.add(cell);
        return this;
    }

    public CustomPdfPTableBuilder withTotalWidth(Float totalWidth) {
        this.totalWidth = totalWidth;
        return this;
    }

    /**
     * Create a pre-configured table builder for footer
     * @param calculation TODO
     * 
     * @return
     */
    public static CustomPdfPTableBuilder createFooterBuilder(InvoiceCalculation calculation) {
        String discountAmount = calculation.getDiscountAmount() == null ? null : calculation.getDiscountAmount().toString();
        String shipping = calculation.getShipping() == null ? null : calculation.getShipping().toString();
        String discountText = calculation.getDiscountText();
        String netGoods = calculation.getNetGoods().toString();
        String net = calculation.getNet().toString();
        String vat = calculation.getVat().toString();
        String paymentConditions = calculation.getPaymentConditions();
        String gross = calculation.getGross().toString();
        
        return createFooterBuilder(discountAmount, shipping, discountText, netGoods, net, vat, paymentConditions, gross);
    }
    
    public static CustomPdfPTableBuilder createFooterBuilder(String netGoods, String vat, String gross){
        return createFooterBuilder(null, null, null, null, netGoods, vat, null, gross);
    }

    private static CustomPdfPTableBuilder createFooterBuilder(
            String discountAmount,
            String shipping,
            String discountText,
            String net,
            String netGoods,
            String vat,
            String paymentConditions,
            String gross) {
        PhraseBuilder bold = new PhraseBuilder("").withFont(FontFactory
                .getFont(PriebesIText5PdfView.FONT,
                        PriebesIText5PdfView.FONT_SIZE,
                        Font.BOLD));
        PhraseBuilder normal = new PhraseBuilder("").withFont(FontFactory
                .getFont(PriebesIText5PdfView.FONT,
                        PriebesIText5PdfView.FONT_SIZE,
                        Font.NORMAL));
        PdfPCellBuilder leftAlign = new PdfPCellBuilder(bold.build());
        PdfPCellBuilder rightAlign = new PdfPCellBuilder(bold.build())
                .withRightHorizontalAlignment();

        ArrayList<TableProperties> rowProperties = new ArrayList<TableProperties>();
        rowProperties.add(new TableProperties("1", Element.ALIGN_LEFT, 30));
        rowProperties.add(new TableProperties("2", Element.ALIGN_RIGHT, 70));

        CustomPdfPTableBuilder footerBuilder = new CustomPdfPTableBuilder(rowProperties);
        if (discountAmount != null && discountText != null){
            
            footerBuilder.addCell(leftAlign
                    .withPhrase(normal.withText("Zwischensumme: ").build())
                    .build())
                    .addCell(rightAlign.withPhrase(
                            normal.withText(net).build()).build());
            
            footerBuilder.addCell(leftAlign
                    .withPhrase(normal.withText("- Rabatt: " + discountText).build())
                    .build())
                    .addCell(rightAlign.withPhrase(
                            normal.withText(discountAmount.toString()).build()).build());
        }
        
        if (shipping != null) footerBuilder
        .addCell(leftAlign
                .withPhrase(normal.withText("Versand").build()).build())
        .addCell(rightAlign
                .withPhrase(normal.withText(shipping).build())
                .build());

        footerBuilder.addCell(leftAlign
                .withPhrase(bold.withText("Betrag netto").build())
                .build())
                .addCell(rightAlign.withPhrase(
                        bold.withText(netGoods).build()).build());

        footerBuilder
                .addCell(leftAlign
                        .withPhrase(bold.withText("zzgl. 19% MwSt.").build())
                        .build())
                .addCell(rightAlign.withPhrase(
                        bold.withText(vat).build()).build());

        footerBuilder
                .addCell(leftAlign.withPhrase(
                        bold.withText("Gesamtbetrag brutto").build())
                        .withBorder(Rectangle.TOP)
                        .build())
                .addCell(rightAlign.withPhrase(
                        bold.withText(gross).build())
                        .withBorder(Rectangle.TOP)
                        .build());

        if (paymentConditions != null) footerBuilder
                .addCell(leftAlign
                        .withPhrase(new PhraseBuilder().withText("Zahlungskonditionen: " + paymentConditions).build())
                        .withBorder(Rectangle.NO_BORDER)
                        .withColSpan(2)
                        .build());

        return footerBuilder;
    }

    public static CustomPdfPTableBuilder createInfoTable(String leftTop,
            String leftBottom,
            String rightTop, String rightBottom) {

        CustomPdfPTableBuilder infoTableBuilder = new CustomPdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithTwoCols())
                .addCell(new PdfPCellBuilder(
                        new PhraseBuilder(leftTop).build()).build())
                .addCell(new PdfPCellBuilder(
                        new PhraseBuilder(rightTop).build()).build())
                .addCell(new PdfPCellBuilder(
                        new PhraseBuilder(leftBottom).build()).build())
                .addCell(new PdfPCellBuilder(
                        new PhraseBuilder(rightBottom).build()).build());
        return infoTableBuilder;
    }

    /*
     * TODO use composite pattern: CustomPdfPTableBuilder -> PdfPTableBuilder ->
     * PdfPCellBuilder -> PhraseBuilder PriebesTable -> Table -> Cell -> Phrase
     */
    public static CustomPdfPTableBuilder createInfoTable(
            String leftTop, String rightTop,
            String leftMiddle, String rightMiddle,
            String leftBottom, String rightBottom) {

        CustomPdfPTableBuilder infoTableBuilder = new CustomPdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithTwoCols())
                .addCell(new PdfPCellBuilder(
                        new PhraseBuilder(leftTop).build()).build())
                .addCell(new PdfPCellBuilder(
                        new PhraseBuilder(rightTop).build()).build())
                .addCell(new PdfPCellBuilder(
                        new PhraseBuilder(leftMiddle).build()).build())
                .addCell(new PdfPCellBuilder(
                        new PhraseBuilder(rightMiddle).build()).build())
                .addCell(new PdfPCellBuilder(
                        new PhraseBuilder(leftBottom).build()).build())
                .addCell(new PdfPCellBuilder(
                        new PhraseBuilder(rightBottom).build()).build());
        return infoTableBuilder;
    }
}
