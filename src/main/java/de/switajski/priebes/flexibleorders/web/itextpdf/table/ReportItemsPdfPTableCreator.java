package de.switajski.priebes.flexibleorders.web.itextpdf.table;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

public abstract class ReportItemsPdfPTableCreator {

    public abstract PdfPTable create(ReportDto cReport) throws DocumentException;

}
