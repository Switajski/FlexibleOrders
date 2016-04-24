package de.switajski.priebes.flexibleorders.itextpdf.table;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.PendingItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;

public class TableWithoutPricesAndWithoutPackageNumbersCreator extends ReportItemsPdfPTableCreator {

    @Override
    public PdfPTable create(ReportDto cReport)
            throws DocumentException {

        PdfPTableBuilder builder = new PdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithFourCols());
        // Refactor - see #71
        for (ReportItem ri : cReport.getItemsByOrder()) {
            if (!(ri instanceof PendingItem)) {
                if (!ri.getOrderItem().isShippingCosts()) {
                    List<String> row = createRow(ri);
                    builder.addBodyRow(row);
                }
            }
        }

        builder.addBreak("Ausstehende Artikel");

        for (ReportItem ri : cReport.getItemsByOrder()) {
            if (ri instanceof PendingItem) {
                builder.addBodyRow(createRow(ri));
            }
        }

        return builder.withFooter(false).build();
    }

    private List<String> createRow(ReportItem ri) {
        List<String> row = new ArrayList<String>();
        // Anzahl
        row.add(String.valueOf(ri.getQuantity()) + " x ");
        // Art.Nr.:
        OrderItem oi = ri.getOrderItem();
        String pNo = oi.getProduct().getProductNumber();
        row.add(sku(pNo));
        // Artikel
        row.add(articleNameWithAdditionalInfo(oi.getAdditionalInfo(), oi.getProduct().getName()));
        // Bestellnr
        row.add(oi.getOrder().getOrderNumber());
        return row;
    }

}
