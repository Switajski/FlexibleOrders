package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.application.AgreementHistory;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;

@Component
public class DeliveryNotesPdfView extends PriebesIText5PdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        DeliveryNotes report = (DeliveryNotes) model.get(DeliveryNotes.class
                .getSimpleName());

        DeliveryHistory history = DeliveryHistory.createWholeFrom(report);
        AgreementHistory aHistory = new AgreementHistory(history);

        String date = "Lieferdatum: "
                + dateFormat.format(report.getCreated());
        String packageNo = "Pakete: " + report.getDocumentNumber();
        String customerNo = "Kundennummer: " + report.getCustomerNumber();
        Address adresse = report.getShippedAddress();
        String heading = "Lieferschein " + report.getDocumentNumber();

        for (Paragraph p : ReportViewHelper.createAddress(adresse))
            document.add(p);
        
        document.add(ReportViewHelper.createDate(date));

        for (Paragraph p : ReportViewHelper.createHeading(heading))
            document.add(p);

        CustomerDetails customerDetails = aHistory.getCustomerDetails();
        if (customerDetails == null) {
            document.add(ReportViewHelper.createInfoTable(
                    packageNo, customerNo, "", date));
        }
        else {
            ExtInfoTableParameter param = new ExtInfoTableParameter();
            ReportViewHelper.mapDocumentNumbersToParam(history, param);
            param.customerDetails = customerDetails;
            param.expectedDelivery = ExpectedDeliveryStringCreator.createDeliveryWeekString(
                    aHistory.getOneAgreementDetail().getExpectedDelivery(), history);
            param.date = date;
            param.customerNo = customerNo;
            param.agreementDetails = aHistory.getOneAgreementDetail();

            document.add(ReportViewHelper.createExtInfoTable(param));
        }

        document.add(ParagraphBuilder.createEmptyLine());
        // insert main table
        document.add(ReportViewHelper.createExtendedTable(report));

    }

}
