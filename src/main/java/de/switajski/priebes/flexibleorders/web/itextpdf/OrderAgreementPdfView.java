package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;

@Component
public class OrderAgreementPdfView extends PriebesIText5PdfView {

    // TODO: make VAT_RATE dependent from order
    public static final Double VAT_RATE = 0.19d;

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        OrderAgreement report = (OrderAgreement) model
                .get(OrderAgreement.class.getSimpleName());

        DeliveryHistory history = DeliveryHistory.of(report);

        String heading = "Auftragsbest" + Unicode.aUml + "tigung "
                + report.getOrderConfirmationNumber()
                + " - best" + Unicode.aUml + "tigt mit " + report.getDocumentNumber();

        String date = "AB-Datum: "
                + dateFormat.format(report.getCreated());
        String customerNo = "Kundennummer: " + report.getCustomerNumber();

        Amount netGoods = AmountCalculator.sum(AmountCalculator
                .getAmountsTimesQuantity(report));
        Amount vat = netGoods.multiply(report.getVatRate());
        Amount gross = netGoods.add(vat);

        for (Paragraph p : ReportViewHelper.createAddress(report.getPurchaseAgreement().getInvoiceAddress()))
            document.add(p);

        document.add(ReportViewHelper.createDate(date));

        for (Paragraph p : ReportViewHelper.createHeading(heading))
            document.add(p);

        if (report.getCustomerDetails() == null) {
            document.add(ReportViewHelper.createInfoTable(
                    customerNo,// rightTop,
                    ExpectedDeliveryStringCreator
                            .createExpectedDeliveryWeekString(report
                                    .getPurchaseAgreement()
                                    .getExpectedDelivery()),// rightBottom,
                    "",// leftTop,
                    date// leftBottom
            ));
        }
        else {
            document.add(ReportViewHelper.createExtInfoTable(
                    new ExtInfoTableParameter(report.getCustomerDetails(), ExpectedDeliveryStringCreator.createDeliveryWeekString(
                            report.getPurchaseAgreement().getExpectedDelivery(),
                            history), report.getPurchaseAgreement(), date, customerNo, history.getOrderNumbers())));
        }

        document.add(ParagraphBuilder.createEmptyLine());

        // insert main table
        document.add(createTable(report));

        // insert footer table
        CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder
                .createFooterBuilder(
                        netGoods, vat, null, gross, null)
                .withTotalWidth(PriebesIText5PdfView.WIDTH);

        PdfPTable footer = footerBuilder.build();

        writeTable(writer, footer);
    }

    private PdfPTable createTable(OrderAgreement cReport)
            throws DocumentException {
        PdfPTableBuilder builder = new PdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithSixCols());
        for (ReportItem he : cReport.getItemsOrdered()) {
            List<String> list = new ArrayList<String>();
            // Art.Nr.:
            Long pNo = he.getOrderItem().getProduct().getProductNumber();
            list.add(pNo.equals(0L) ? "n.a." : pNo.toString());
            // Artikel
            list.add(he.getOrderItem().getProduct().getName());
            // Anzahl
            list.add(String.valueOf(he.getQuantity()));
            // EK per Stueck
            list.add(he.getOrderItem().getNegotiatedPriceNet().toString());
            // Bestellnr
            list.add(he.getOrderItem().getOrder().getOrderNumber());
            // gesamt
            list.add(he
                    .getOrderItem()
                    .getNegotiatedPriceNet()
                    .multiply(he.getQuantity())
                    .toString());

            builder.addBodyRow(list);
        }

        return builder.withFooter(false).build();
    }

}
