package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.SimpleTableHeaderCreator;

/**
 * Pdf view customized for the display of an order
 *
 * @author Marek
 *
 */
@Component
public class OrderPdfView extends PriebesIText5PdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ReportDto report = (ReportDto) model.get(ReportDto.class.getSimpleName());

        Address adresse = report.invoiceSpecific_headerAddress;
        String heading = "Bestellung";

        Amount netGoods = report.netGoods;
        Amount vat = netGoods.multiply(Order.VAT_RATE);
        Amount gross = netGoods.add(vat);

        for (Element p : ReportViewHelper.createAddress(adresse)) {
            document.add(p);
        }

        for (Paragraph p : ReportViewHelper.createHeading(heading)) {
            document.add(p);
        }

        document.add(new SimpleTableHeaderCreator().create(report));
        document.add(ParagraphBuilder.createEmptyLine());

        // insert main table
        document.add(createTable(report));

        // insert footer table
        if (hasRecommendedPrices(report) && hasVat(report)) {
            CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder
                    .createFooterBuilder(netGoods.toString(), vat.toString(), gross.toString())
                    .withTotalWidth(PriebesIText5PdfView.WIDTH);

            PdfPTable footer = footerBuilder.build();

            footer.writeSelectedRows(0, -1,
                    /* xPos */PriebesIText5PdfView.PAGE_MARGIN_LEFT,
                    /* yPos */PriebesIText5PdfView.PAGE_MARGIN_BOTTOM
                            + FOOTER_MARGIN_BOTTOM,
                    writer.getDirectContent());
        }

    }

    private PdfPTable createTable(ReportDto order) throws DocumentException {

        PdfPTableBuilder builder = new PdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithFiveCols());
        for (OrderItem oi : order.getOrderItemsByOrder()) {
            if (!oi.isShippingCosts()) {
                List<String> row = new ArrayList<String>();
                Product product = oi.getProduct();
                String pNoStr = (product.hasProductNo()) ? product.getProductNumber().toString() : "n.a.";
                row.add(pNoStr);
                // Artikel
                row.add(product.getName());
                // Anzahl
                row.add(String.valueOf(oi.getOrderedQuantity()));
                // EK per Stueck
                row.add(getPriceString(oi));
                // gesamt
                row.add(getPriceXquantity(oi));
                builder.addBodyRow(row);
            }
        }
        return builder.withFooter(false).build();
    }

    private String getPriceXquantity(OrderItem he) {
        if (he.getNegotiatedPriceNet() != null
                && he.getNegotiatedPriceNet().getValue() != null) return he
                .getNegotiatedPriceNet()
                .multiply(he.getOrderedQuantity())
                .toString();
        else return "-";
    }

    private String getPriceString(OrderItem he) {
        String priceString = "";
        if (he.getNegotiatedPriceNet() != null
                && he.getOrderedQuantity() != null
                && he.getNegotiatedPriceNet().getValue() != null) priceString = he.getNegotiatedPriceNet().toString();
        else priceString = "Preis n. verf.";
        return priceString;
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
