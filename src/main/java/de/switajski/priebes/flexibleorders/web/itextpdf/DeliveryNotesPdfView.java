package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.ExtendedTableHeaderCreator;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.ReportItemsPdfPTableCreator;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.SimpleTableHeaderCreator;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.TableWithPricesAndWithPackageNumberCreator;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.TableWithPricesAndWithoutPackageNumberCreator;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.TableWithoutPricesAndWithPackageNumbersCreator;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.TableWithoutPricesAndWithoutPackageNumbersCreator;

@Component
public class DeliveryNotesPdfView extends PriebesIText5PdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ReportDto report = (ReportDto) model.get(ReportDto.class.getSimpleName());

        String date = "Lieferdatum: "
                + dateFormat.format(report.created);
        String packageNo = "Paket: " + report.shippingSpecific_packageNumber;
        String customerNo = "Kundennummer: " + report.customerNumber;
        Address adresse = report.shippingSpecific_shippingAddress;
        String heading = "Lieferschein " + report.documentNumber;

        for (Element p : ReportViewHelper.createAddress(adresse, createLogo()))
            document.add(p);

        document.add(ReportViewHelper.createDate(date));

        for (Paragraph p : ReportViewHelper.createHeading(heading))
            document.add(p);

        if (report.isShowExtendedInformation()) {
            ExtInfoTableParameter param = new ExtInfoTableParameter(report);
            param.packageNumber = report.shippingSpecific_packageNumber;
            param.trackNumber = report.shippingSpecific_trackNumber;
            param.deliveryNotesNumbers = null;
            param.invoiceNumbers = null;
            param.creditNoteNumbers = null;
            document.add(new ExtendedTableHeaderCreator().create(param));
        }
        else {
            document.add(new SimpleTableHeaderCreator().create(
                    packageNo, customerNo, "", ""));
        }

        document.add(ParagraphBuilder.createEmptyLine());
        // insert main table
        boolean hasPackageNumbers = hasPackageNumbers(report);
        ReportItemsPdfPTableCreator creator;
        if (report.showPricesInDeliveryNotes) {
            if (hasPackageNumbers) {
                creator = new TableWithPricesAndWithPackageNumberCreator();
            }
            else {
                creator = new TableWithPricesAndWithoutPackageNumberCreator();
            }
        }
        else {
            if (hasPackageNumbers) {
                creator = new TableWithoutPricesAndWithPackageNumbersCreator();
            }
            else {
                creator = new TableWithoutPricesAndWithoutPackageNumbersCreator();
            }
        }
        document.add(creator.create(report));

    }

}
