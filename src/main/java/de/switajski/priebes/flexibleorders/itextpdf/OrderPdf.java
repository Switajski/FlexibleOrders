package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.itextpdf.table.SimpleTableHeaderCreator;
import de.switajski.priebes.flexibleorders.itextpdf.table.TableForOrderCreator;
import de.switajski.priebes.flexibleorders.itextpdf.template.BusinessLetterPdfTemplate;

/**
 * Pdf view customized for the display of an order
 *
 * @author Marek
 *
 */
public class OrderPdf implements PdfDocumentAppender {

    private ReportDto report;
    private PdfWriter writer;

    public OrderPdf(ReportDto report, PdfWriter writer) {
        this.report = report;
        this.writer = writer;
    }

    @Override
    public void append(Document document) throws DocumentException {

        Amount netGoods = report.netGoods;
        Amount vat = netGoods.multiply(Order.VAT_RATE);
        Amount gross = netGoods.add(vat);

        document.add(new SimpleTableHeaderCreator().create(report));
        document.add(ParagraphBuilder.createEmptyLine());

        // insert main table
        document.add(new TableForOrderCreator().create(report));

        // insert footer table
        if (hasRecommendedPrices(report) && hasVat(report)) {
            CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder
                    .createFooterBuilder(netGoods.toString(), vat.toString(), gross.toString())
                    .withTotalWidth(BusinessLetterPdfTemplate.WIDTH);

            PdfPTable footer = footerBuilder.build();

            footer.writeSelectedRows(
                    0,
                    -1,
                    /* xPos */BusinessLetterPdfTemplate.PAGE_MARGIN_LEFT,
                    /* yPos */BusinessLetterPdfTemplate.PAGE_MARGIN_BOTTOM
                            + BusinessLetterPdfTemplate.FOOTER_MARGIN_BOTTOM,
                    writer.getDirectContent());
        }

    }

    private boolean hasVat(ReportDto order) {
        return !(order.vatRate == null);
    }

    private boolean hasRecommendedPrices(ReportDto order) {
        for (OrderItem oi : order.orderItems) {
            if (oi.getNegotiatedPriceNet() == null
                    || oi.getNegotiatedPriceNet().getValue() == null) return false;
        }
        return true;
    }

}
