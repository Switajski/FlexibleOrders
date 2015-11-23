package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.InvoiceCalculation;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.ExtendedTableHeaderCreator;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.TableForInvoiceCreator;

@Component
public class InvoicePdfView extends PriebesIText5PdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ReportDto report = (ReportDto) model.get(ReportDto.class.getSimpleName());

        String customerNo = report.customerNumber.toString();
        String date = "Rechnungsdatum: "
                + dateFormat.format(report.created);
        String heading = "Rechnung " + report.documentNumber;

        InvoiceCalculation calculation = new InvoiceCalculation(report);

        for (Element p : ReportViewHelper.createAddress(report.invoiceSpecific_headerAddress, this.createLogo()))
            document.add(p);

        document.add(ReportViewHelper.createDate(date));

        for (Paragraph p : ReportViewHelper.createHeading(heading))
            document.add(p);

        ExtInfoTableParameter param = new ExtInfoTableParameter();
        param.orderNumbers = report.related_orderNumbers;
        param.orderConfirmationNumbers = report.related_orderConfirmationNumbers;
        param.deliveryNotesNumbers = report.related_deliveryNotesNumbers;

        param.vendorNumber = report.customerSpecific_vendorNumber;
        param.contactInformation = report.customerSpecific_contactInformation;
        param.mark = report.customerSpecific_mark;
        param.vatIdNo = report.customerSpecific_vatIdNo;
        param.date = date;
        param.customerNo = customerNo;
        param.billing = StringUtils.isEmpty(report.invoiceSpecific_billing) ? "" : "Abrechnung: " + report.invoiceSpecific_billing;

        document.add(new ExtendedTableHeaderCreator().create(param));

        document.add(ParagraphBuilder.createEmptyLine());

        // insert main table
        document.add(new TableForInvoiceCreator().create(report));

        // insert footer table
        CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder
                .createFooterBuilder(calculation, report.orderConfirmationSpecific_paymentConditions)
                .withTotalWidth(PriebesIText5PdfView.WIDTH);

        if (report.orderConfirmationSpecific_paymentConditions != null) {
            addPaymentConditions(report.orderConfirmationSpecific_paymentConditions, footerBuilder);
        }

        PdfPTable footer = footerBuilder.build();

        footer.writeSelectedRows(0, -1,
                PriebesIText5PdfView.PAGE_MARGIN_LEFT, // xPos
                footerYPos(), // yPos
                writer.getDirectContent());

    }

    protected int footerYPos() {
        return PriebesIText5PdfView.PAGE_MARGIN_BOTTOM + PriebesIText5PdfView.FOOTER_MARGIN_BOTTOM;
    }

}
