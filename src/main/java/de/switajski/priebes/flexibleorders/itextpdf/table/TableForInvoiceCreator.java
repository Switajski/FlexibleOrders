package de.switajski.priebes.flexibleorders.itextpdf.table;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ColumnFormat;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.itextpdf.template.BusinessLetterPdfTemplate;
import de.switajski.priebes.flexibleorders.reference.ProductType;

public class TableForInvoiceCreator extends ReportItemsPdfPTableCreator {

    @Override
    public PdfPTable create(ReportDto cReport)
            throws DocumentException {

        PdfPTableBuilder builder = new PdfPTableBuilder(createTableProperties());
        for (ReportItem ri : cReport.getItemsByOrder()) {
            if (ri.getOrderItem().getProduct().getProductType() != ProductType.SHIPPING) {
                List<String> list = new ArrayList<String>();
                // Art.Nr.:
                String pNo = ri.getOrderItem().getProduct().getProductNumber();
                list.add(sku(pNo));
                // Artikel
                list.add(ri.getOrderItem().getProduct().getName());
                // Anzahl
                list.add(String.valueOf(ri.getQuantity()));
                // EK per Stueck
                list.add(ri.getOrderItem().getNegotiatedPriceNet().toString());
                // Lieferscheinnr.
                if (ri.getPredecessor() != null && ri.getPredecessor().getReport() != null) {
                    list.add(ri.getPredecessor().getReport().getDocumentNumber());
                }
                else list.add(" ");
                // gesamt
                list.add(ri.getOrderItem()
                        .getNegotiatedPriceNet()
                        .multiply(ri.getQuantity())
                        .toString());

                builder.addBodyRow(list);
            }
        }

        return builder.withFooter(false).build();
    }

    private ArrayList<ColumnFormat> createTableProperties() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat("Art. Nr.", Element.ALIGN_LEFT, 10));
        rowProperties.add(new ColumnFormat("Artikel", Element.ALIGN_LEFT, 40));
        rowProperties.add(new ColumnFormat("Anzahl", Element.ALIGN_LEFT, 7));
        rowProperties.add(new ColumnFormat("EK per St" + Unicode.U_UML + "ck", Element.ALIGN_LEFT, 13));
        rowProperties.add(new ColumnFormat("Lieferscheinnr.", Element.ALIGN_LEFT, 18, BusinessLetterPdfTemplate.eightSizeFont));
        rowProperties.add(new ColumnFormat("gesamt", Element.ALIGN_RIGHT, 12));
        return rowProperties;
    }

}
