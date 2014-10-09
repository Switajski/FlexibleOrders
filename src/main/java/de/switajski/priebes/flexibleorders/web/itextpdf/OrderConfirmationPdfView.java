package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;

@Component
public class OrderConfirmationPdfView extends PriebesIText5PdfView {

    // TODO: make VAT_RATE dependent from order
    public static final Double VAT_RATE = 0.19d;

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ReportDto report = (ReportDto) model
                .get(ReportDto.class.getSimpleName());

        String heading = "Auftragsbest" + Unicode.aUml + "tigung " + report.documentNumber.toString();
        if (report.orderConfirmationNumber != null)
            heading += " - best"+Unicode.aUml+"tigt mit "+report.orderConfirmationNumber;

        String date = "AB-Datum: " + dateFormat.format(report.created);
        String customerNo = "Kundennummer: " + report.customerNumber;

        Amount netGoods = report.netGoods;
        Amount vat = netGoods.multiply(report.vatRate);
        Amount gross = netGoods.add(vat);

        for (Paragraph p : ReportViewHelper.createAddress(report.headerAddress))
            document.add(p);

        document.add(ReportViewHelper.createDate(date));

        for (Paragraph p : ReportViewHelper.createHeading(heading))
            document.add(p);

        PdfPTable infoTable = report.isShowExtendedInformation() ?
                ReportViewHelper.createExtInfoTable(new ExtInfoTableParameter(report)) :
                ReportViewHelper.createInfoTable(
                        customerNo,
                        removeNull(report.customerFirstName) + " " + removeNull(report.customerLastName),
                        ExpectedDeliveryStringCreator.createExpectedDeliveryWeekString(
                                report.shippingSpecific_expectedDelivery),
                        "");
        document.add(infoTable);

        document.add(ParagraphBuilder.createEmptyLine());

        // insert main table
        document.add(ReportViewHelper.createExtendedTable(report));

        // insert footer table
        CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder
                .createFooterBuilder(
                        netGoods, vat, null, gross, null)
                .withTotalWidth(PriebesIText5PdfView.WIDTH);

        PdfPTable footer = footerBuilder.build();

        footer.writeSelectedRows(0, -1,
                /* xPos */PriebesIText5PdfView.PAGE_MARGIN_LEFT,
                /* yPos */PriebesIText5PdfView.PAGE_MARGIN_BOTTOM
                        + FOOTER_MARGIN_BOTTOM,
                writer.getDirectContent());
    }

    private String removeNull(String customerLastName) {
        if (customerLastName == null)
            return "";
        return customerLastName;
    }

}
