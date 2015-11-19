package de.switajski.priebes.flexibleorders.web.itextpdf.table;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ColumnFormat;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.PriebesIText5PdfView;

public class TableWithPricesAndWithPackageNumberCreator extends ReportItemsPdfPTableCreator {

    @Override
    public PdfPTable create(ReportDto report)
            throws DocumentException {
        PdfPTableBuilder builder = new PdfPTableBuilder(
                tableProperties());
        // Refactor - see #71
        for (ReportItem he : report.getItemsByOrder()) {
            List<String> list = new ArrayList<String>();
            // Art.Nr.
            Product product = he.getOrderItem().getProduct();
            list.add(product.hasProductNo() ? product.getProductNumber() : "n.a.");
            // Artikel
            list.add(product.getName());
            // Paketnr.
            if (he instanceof ShippingItem) {
                list.add(((ShippingItem) he).getPackageNumber());
            }
            else {
                list.add("");
            }
            // Anzahl
            list.add(String.valueOf(he.getQuantity()));
            // EK per Stueck
            list.add(he.getOrderItem().getNegotiatedPriceNet().toString());
            // Bestellnr
            list.add(he.getOrderItem().getOrder().getOrderNumber());
            // gesamt
            list.add(he.getOrderItem().getNegotiatedPriceNet()
                    .multiply(he.getQuantity()).toString());

            builder.addBodyRow(list);
        }

        return builder.withFooter(false).build();
    }

    private ArrayList<ColumnFormat> tableProperties() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat("Art. Nr.", Element.ALIGN_LEFT, 10));
        rowProperties.add(new ColumnFormat("Artikel", Element.ALIGN_LEFT, 40));
        rowProperties.add(new ColumnFormat("Paketnr.", Element.ALIGN_LEFT, 8));
        rowProperties.add(new ColumnFormat("Anzahl", Element.ALIGN_LEFT, 7));
        rowProperties.add(new ColumnFormat("EK per Stk.", Element.ALIGN_LEFT, 10));
        rowProperties.add(new ColumnFormat("Bestellnr.", Element.ALIGN_LEFT, 18, PriebesIText5PdfView.eightSizeFont));
        rowProperties.add(new ColumnFormat("gesamt", Element.ALIGN_RIGHT, 12));
        return rowProperties;
    }

}
