package de.switajski.priebes.flexibleorders.itextpdf.table;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.PendingItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ColumnFormat;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.template.BusinessLetterPdfTemplate;

public class TableWithoutPricesAndWithPackageNumbersCreator extends ReportItemsPdfPTableCreator {

    @Override
    public PdfPTable create(ReportInPdf cReport) throws DocumentException {
        PdfPTableBuilder builder = new PdfPTableBuilder(tableProperties());

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

    private ArrayList<ColumnFormat> tableProperties() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat("Anzahl", Element.ALIGN_RIGHT, 10));
        rowProperties.add(new ColumnFormat("Art. Nr.", Element.ALIGN_LEFT, 10));
        rowProperties.add(new ColumnFormat("Artikel", Element.ALIGN_LEFT, 50));
        rowProperties.add(new ColumnFormat("Paketnr.", Element.ALIGN_LEFT, 8));
        rowProperties.add(new ColumnFormat("Bestellnr.", Element.ALIGN_RIGHT, 20, BusinessLetterPdfTemplate.eightSizeFont));
        return rowProperties;
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
        // Paketnr.
        if (ri instanceof ShippingItem) {
            row.add(((ShippingItem) ri).getPackageNumber());
        }
        else row.add("");
        // Bestellnr
        row.add(oi.getOrder().getOrderNumber());
        return row;
    }

}
