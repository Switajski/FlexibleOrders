package de.switajski.priebes.flexibleorders.itextpdf.table;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;

public abstract class ReportItemsPdfPTableCreator {

    String stringInsteadSku = " ";

    public abstract PdfPTable create(ReportInPdf cReport) throws DocumentException;

    protected String sku(String pNo) {
        return pNo == null || pNo.equals(0L) ? stringInsteadSku : pNo.toString();
    }

    protected String articleNameWithAdditionalInfo(String additionalInfo, String productName) {
        StringBuilder articleName = new StringBuilder().append(productName);
        if (additionalInfo != null) articleName.append("\n").append(additionalInfo);
        return articleName.toString();
    }

}
