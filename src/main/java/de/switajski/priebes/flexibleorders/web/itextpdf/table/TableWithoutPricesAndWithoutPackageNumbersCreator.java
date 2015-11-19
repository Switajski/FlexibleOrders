package de.switajski.priebes.flexibleorders.web.itextpdf.table;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.report.PendingItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

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
        String pNo = ri.getOrderItem().getProduct().getProductNumber();
        row.add(pNo.equals("0") ? "n.a." : pNo.toString());
        // Artikel
        row.add(ri.getOrderItem().getProduct().getName());
        // Bestellnr
        row.add(ri.getOrderItem().getOrder().getOrderNumber());
        return row;
    }

}
