package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.itextpdf.table.SimpleTableHeaderCreator;
import de.switajski.priebes.flexibleorders.itextpdf.table.TableForOrderCreator;

/**
 * Pdf view customized for the display of an order
 *
 * @author Marek
 *
 */
public class OrderPdf implements PdfDocumentAppender {

    private Image logo;
    private ReportDto report;
    private PdfWriter writer;

    public OrderPdf(Image logo, ReportDto report, PdfWriter writer) {
        this.logo = logo;
        this.report = report;
        this.writer = writer;
    }

    @Override
    public void append(Document document) throws DocumentException {

        Address adresse = report.invoiceSpecific_headerAddress;
        String heading = "Bestellung " + report.documentNumber;

        Amount netGoods = report.netGoods;
        Amount vat = netGoods.multiply(Order.VAT_RATE);
        Amount gross = netGoods.add(vat);

        for (Element p : ReportViewHelper.createHeaderWithAddress(adresse, logo)) {
            document.add(p);
        }

        for (Paragraph p : ReportViewHelper.createHeading(heading)) {
            document.add(p);
        }

        document.add(new SimpleTableHeaderCreator().create(report));
        document.add(ParagraphBuilder.createEmptyLine());

        // insert main table
        document.add(new TableForOrderCreator().create(report));

        // insert footer table
        if (hasRecommendedPrices(report) && hasVat(report)) {
            CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder
                    .createFooterBuilder(netGoods.toString(), vat.toString(), gross.toString())
                    .withTotalWidth(PriebesIText5PdfView.WIDTH);

            PdfPTable footer = footerBuilder.build();

            footer.writeSelectedRows(0, -1,
                    /* xPos */PriebesIText5PdfView.PAGE_MARGIN_LEFT,
                    /* yPos */PriebesIText5PdfView.PAGE_MARGIN_BOTTOM
                            + PriebesIText5PdfView.FOOTER_MARGIN_BOTTOM,
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
