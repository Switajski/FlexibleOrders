package de.switajski.priebes.flexibleorders.itextpdf.table;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;

public abstract class ReportItemsPdfPTableCreator {

    String stringInsteadSku = " ";

    public abstract PdfPTable create(ReportDto cReport) throws DocumentException;

    protected String sku(String pNo) {
        return pNo == null || pNo.equals(0L) ? stringInsteadSku : pNo.toString();
    }

}