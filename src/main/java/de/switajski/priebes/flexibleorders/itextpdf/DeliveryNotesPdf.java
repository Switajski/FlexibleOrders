package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.parameter.ExtInfoTableParameter;
import de.switajski.priebes.flexibleorders.itextpdf.table.ExtendedTableHeaderCreator;
import de.switajski.priebes.flexibleorders.itextpdf.table.ReportItemsPdfPTableCreator;
import de.switajski.priebes.flexibleorders.itextpdf.table.SimpleTableHeaderCreator;
import de.switajski.priebes.flexibleorders.itextpdf.table.TableWithPricesAndWithPackageNumberCreator;
import de.switajski.priebes.flexibleorders.itextpdf.table.TableWithPricesAndWithoutPackageNumberCreator;
import de.switajski.priebes.flexibleorders.itextpdf.table.TableWithoutPricesAndWithPackageNumbersCreator;
import de.switajski.priebes.flexibleorders.itextpdf.table.TableWithoutPricesAndWithoutPackageNumbersCreator;

public class DeliveryNotesPdf implements PdfDocumentAppender {

    private PdfUtils pdfUtils;
    private ReportInPdf report;

    public DeliveryNotesPdf(ReportInPdf report) {
        this.report = report;
        this.pdfUtils = new PdfUtils();
    }

    @Override
    public void append(Document document) throws DocumentException {

        String packageNo = "Paket: " + report.shippingSpecific_packageNumber;
        String customerNo = "Kundennummer: " + report.customerNumber;

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
                    packageNo,
                    customerNo,
                    "",
                    ""));
        }

        document.add(ParagraphBuilder.createEmptyLine());
        // insert main table
        boolean hasPackageNumbers = pdfUtils.hasPackageNumbers(report);
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
