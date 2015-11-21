package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.InvoiceCalculation;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.ExtendedTableHeaderCreator;

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
        param.invoiceNumbers = report.related_invoiceNumbers;
        param.orderAgreementNumbers = report.related_orderAgreementNumbers;
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
        if (report.invoiceSpecific_hasItemsWithDifferentCreationDates) document.add(createTableWithDeliveryNotes(report.items));
        else document.add(createTable(report));

        // insert footer table
        CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder
                .createFooterBuilder(calculation, report.orderConfirmationSpecific_paymentConditions)
                .withTotalWidth(PriebesIText5PdfView.WIDTH);

        if (report.orderConfirmationSpecific_paymentConditions != null) {
            addPaymentConditions(report.orderConfirmationSpecific_paymentConditions, footerBuilder);
        }

        PdfPTable footer = footerBuilder.build();

        footer.writeSelectedRows(0, -1,
                /* xPos */PriebesIText5PdfView.PAGE_MARGIN_LEFT,
                /* yPos */PriebesIText5PdfView.PAGE_MARGIN_BOTTOM
                        + FOOTER_MARGIN_BOTTOM,
                writer.getDirectContent());

    }

    private PdfPTable createTable(ReportDto cReport) throws DocumentException {
        PdfPTableBuilder builder = new PdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithSixCols());
        for (ReportItem he : cReport.getItemsByOrder()) {
            if (he.getOrderItem().getProduct().getProductType() != ProductType.SHIPPING) {
                List<String> list = new ArrayList<String>();
                // Art.Nr.:
                String pNo = he.getOrderItem().getProduct().getProductNumber();
                list.add(pNo == null || pNo.equals(0L) ? "n.a." : pNo.toString());
                // Artikel
                list.add(he.getOrderItem().getProduct().getName());
                // Anzahl
                list.add(String.valueOf(he.getQuantity()));
                // EK per Stueck
                list.add(he.getOrderItem().getNegotiatedPriceNet().toString());
                // Bestellnr
                list.add(he.getOrderItem().getOrder().getOrderNumber());
                // gesamt
                list.add(he.getOrderItem()
                        .getNegotiatedPriceNet()
                        .multiply(he.getQuantity())
                        .toString());

                builder.addBodyRow(list);
            }
        }

        return builder.withFooter(false).build();
    }

    public static PdfPTable createTableWithDeliveryNotes(Collection<ReportItem> cReport) throws DocumentException {
        PdfPTableBuilder builder = new PdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithSevenCols());
        for (ReportItem ii : cReport) {
            Set<ShippingItem> sis = DeliveryHistory.of(ii.getOrderItem()).reportItems(ShippingItem.class);

            if (ii.getOrderItem().getProduct().getProductType() != ProductType.SHIPPING) {
                List<String> list = new ArrayList<String>();
                // Art.Nr.:
                String pNo = ii.getOrderItem().getProduct().getProductNumber();
                list.add(pNo == null || pNo.equals(0L) ? "n.a." : pNo.toString());
                // Artikel
                list.add(ii.getOrderItem().getProduct().getName());
                // Anzahl
                list.add(String.valueOf(ii.getQuantity()));
                // EK per Stueck
                list.add(ii.getOrderItem().getNegotiatedPriceNet().toString());
                // Lieferscheinnr.
                list.add(ReportViewHelper.documentNumbersOf(sis));
                // Lieferdatum
                list.add(ReportViewHelper.createdDatesOf(sis));
                // gesamt
                list.add(ii.getOrderItem()
                        .getNegotiatedPriceNet()
                        .multiply(ii.getQuantity())
                        .toString());

                builder.addBodyRow(list);
            }
        }

        return builder.withFooter(false).build();
    }

}
