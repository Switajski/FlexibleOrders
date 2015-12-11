package de.switajski.priebes.flexibleorders.itextpdf.table;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;

public class TableForOrderCreator extends ReportItemsPdfPTableCreator {

    @Override
    public PdfPTable create(ReportDto order) throws DocumentException {

        PdfPTableBuilder builder = new PdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithFiveCols());
        for (OrderItem oi : order.getOrderItemsByOrder()) {
            if (!oi.isShippingCosts()) {
                List<String> row = new ArrayList<String>();
                Product product = oi.getProduct();
                row.add(sku(product.getProductNumber()));
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

}
