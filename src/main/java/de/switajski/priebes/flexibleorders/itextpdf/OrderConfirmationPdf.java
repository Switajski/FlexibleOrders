package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.itextpdf.parameter.ExtInfoTableParameter;
import de.switajski.priebes.flexibleorders.itextpdf.table.ExtendedTableHeaderCreator;
import de.switajski.priebes.flexibleorders.itextpdf.table.SimpleTableHeaderCreator;
import de.switajski.priebes.flexibleorders.itextpdf.table.TableWithPricesAndWithoutPackageNumberCreator;
import de.switajski.priebes.flexibleorders.itextpdf.template.BusinessLetterPdfTemplate;

public class OrderConfirmationPdf implements PdfDocumentAppender {

    // TODO: make VAT_RATE dependent from order
    public static final Double VAT_RATE = 0.19d;
    private PdfWriter writer;
    private ReportDto report;
    private PdfUtils pdfUtils;

    public OrderConfirmationPdf(ReportDto report, PdfWriter writer) {
        this.report = report;
        this.writer = writer;
        this.pdfUtils = new PdfUtils();
    }

    @Override
    public void append(Document document) throws DocumentException {

        String customerNo = "Kundennummer: " + report.customerNumber;

        Amount netGoods = report.netGoods;
        Amount vat = netGoods.multiply(report.vatRate);
        Amount gross = netGoods.add(vat);

        PdfPTable infoTable = report.isShowExtendedInformation() ? new ExtendedTableHeaderCreator()
                .create(new ExtInfoTableParameter(report)) : new SimpleTableHeaderCreator().create(
                        customerNo,
                        removeNull(report.customerFirstName) + " " + removeNull(report.customerLastName),
                        ExpectedDeliveryStringCreator.createExpectedDeliveryWeekString(
                                report.shippingSpecific_expectedDelivery),
                        "");
        document.add(infoTable);

        document.add(ParagraphBuilder.createEmptyLine());

        // insert main table
        document.add(new TableWithPricesAndWithoutPackageNumberCreator().create(report));

        // insert footer table
        CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder
                .createFooterBuilder(netGoods.toString(), vat.toString(), gross.toString())
                .withTotalWidth(BusinessLetterPdfTemplate.WIDTH);

        if (report.orderConfirmationSpecific_paymentConditions != null) {
            pdfUtils.addPaymentConditions(report.orderConfirmationSpecific_paymentConditions, footerBuilder);
        }

        PdfPTable footer = footerBuilder.build();

        footer.writeSelectedRows(
                0,
                -1,
                /* xPos */BusinessLetterPdfTemplate.PAGE_MARGIN_LEFT,
                /* yPos */BusinessLetterPdfTemplate.PAGE_MARGIN_BOTTOM
                        + BusinessLetterPdfTemplate.FOOTER_MARGIN_BOTTOM,
                writer.getDirectContent());
    }

    private String removeNull(String customerLastName) {
        if (customerLastName == null) return "";
        return customerLastName;
    }

}
