package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.table.SimpleTableHeaderCreator;
import de.switajski.priebes.flexibleorders.itextpdf.table.TableForPendingItemsCreator;

public class ToBeShippedPdf implements PdfDocumentAppender {

    private ReportInPdf report;

    public ToBeShippedPdf(ReportInPdf report) {
        this.report = report;
    }

    @Override
    public void append(Document document) throws DocumentException {

        String stringWithoutNull = toStringWithoutNull(report.customerNumber);
        if (!stringWithoutNull.equals("")) stringWithoutNull = "Kundennr.: " + stringWithoutNull;
        document.add(new SimpleTableHeaderCreator().create(
                toStringWithoutNull(report.customerFirstName) + " " + toStringWithoutNull(report.customerLastName),
                stringWithoutNull,
                "",
                ""));

        document.add(ParagraphBuilder.createEmptyLine());

        // insert main table
        document.add(new TableForPendingItemsCreator().create(report));

    }

    private String toStringWithoutNull(Object string) {
        if (string == null) return "";
        else return string.toString();
    }

}
