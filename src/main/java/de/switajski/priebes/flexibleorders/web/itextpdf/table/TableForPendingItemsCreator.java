package de.switajski.priebes.flexibleorders.web.itextpdf.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.itextpdf.builder.ColumnFormat;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.dto.ReportItemInPdf;

public class TableForPendingItemsCreator extends ReportItemsPdfPTableCreator {

    @Override
    public PdfPTable create(ReportDto cReport)
            throws DocumentException {

        PdfPTableBuilder builder = new PdfPTableBuilder(
                tableProperties());
        // Refactor - see #71
        String customerNumber = "";
        for (ReportItemInPdf ri : cReport.itemDtos) {
            if (!ri.customerNumber.equals(customerNumber)) {
                if (hasManyCustomers(cReport)) addBreak(builder, ri);
                customerNumber = ri.customerNumber;
            }
            List<String> row = createRow(ri);
            builder.addBodyRow(row);
        }

        return builder.withFooter(false).build();
    }

    private boolean hasManyCustomers(ReportDto report) {
        Set<String> customerNumbers = new HashSet<String>();
        for (ReportItemInPdf item : report.itemDtos) {
            customerNumbers.add(item.customerNumber);
        }
        return customerNumbers.size() > 1;
    }

    private void addBreak(PdfPTableBuilder builder, ReportItemInPdf ri) {
        String companyName = ri.companyName;
        if (companyName == null) companyName = "";

        String breakText = new StringBuilder().append(ri.customerNumber).append(" ").append(companyName).toString();
        builder.addBreak(breakText);
    }

    private ArrayList<ColumnFormat> tableProperties() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat("Anzahl", Element.ALIGN_RIGHT, 10));
        rowProperties.add(new ColumnFormat("Art. Nr.", Element.ALIGN_LEFT, 10));
        rowProperties.add(new ColumnFormat("Artikel", Element.ALIGN_LEFT, 50));
        rowProperties.add(new ColumnFormat("Kundennr", Element.ALIGN_LEFT, 10));
        rowProperties.add(new ColumnFormat("Bestellnr.", Element.ALIGN_RIGHT, 10));
        return rowProperties;
    }

    private List<String> createRow(ReportItemInPdf ri) {
        List<String> row = new ArrayList<String>();
        // Anzahl
        row.add(String.valueOf(ri.quantity) + " x ");
        // Art.Nr.:
        String pNo = ri.sku;
        row.add(sku(pNo));
        // Artikel
        row.add(ri.productName);
        // Kundennr.
        row.add(ri.customerNumber);
        // Bestellnr
        row.add(ri.orderNumber);
        return row;
    }
}
