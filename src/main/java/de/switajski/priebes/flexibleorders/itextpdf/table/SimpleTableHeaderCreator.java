package de.switajski.priebes.flexibleorders.itextpdf.table;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.itextpdf.PdfUtils;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;

public class SimpleTableHeaderCreator {

    private String leftBottom, leftTop, rightBottom, rightTop;

    public PdfPTable create(ReportDto report) throws DocumentException {
        rightTop = replaceNull(report.customerFirstName) + " " + replaceNull(report.customerLastName);
        rightBottom = "Kundennummer: "
                + report.customerNumber;
        leftTop = "Bestellnummer: " + report.documentNumber.toString();
        leftBottom = "Bestelldatum: "
                + PdfUtils.dateFormat.format(report.created);

        CustomPdfPTableBuilder infoTableBuilder = CustomPdfPTableBuilder
                .createInfoTable(leftTop, leftBottom, rightTop, rightBottom);
        PdfPTable infoTable = infoTableBuilder.build();
        infoTable.setWidthPercentage(100);

        return infoTable;
    }

    public PdfPTable create(String rightTop,
            String rightBottom, String leftTop, String leftBottom)
            throws DocumentException {

        CustomPdfPTableBuilder infoTableBuilder = CustomPdfPTableBuilder
                .createInfoTable(leftTop, leftBottom, rightTop, rightBottom);
        PdfPTable infoTable = infoTableBuilder.build();
        infoTable.setWidthPercentage(100);

        return infoTable;
    }

    private String replaceNull(String customerFirstName) {
        if (customerFirstName == null) return "";
        return customerFirstName;
    }

}
