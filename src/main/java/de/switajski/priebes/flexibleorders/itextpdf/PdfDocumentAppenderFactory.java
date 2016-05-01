package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.dto.DeliveryNotesInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.InvoiceInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderConfirmationInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ToBeShippedInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ToBeShippedToOneCustomerInPdf;

public class PdfDocumentAppenderFactory {

    private PdfWriter writer;

    public PdfDocumentAppenderFactory(PdfWriter writer) {
        this.writer = writer;
    }

    public PdfDocumentAppender create(ReportInPdf reportDto) {
        PdfDocumentAppender pdfDocumentAppender = null;
        if (reportDto instanceof DeliveryNotesInPdf) {
            pdfDocumentAppender = new DeliveryNotesPdf(reportDto);
        }
        else if (reportDto instanceof InvoiceInPdf) {
            pdfDocumentAppender = new InvoicePdf(reportDto, writer);
        }
        else if (reportDto instanceof OrderConfirmationInPdf) {
            pdfDocumentAppender = new OrderConfirmationPdf(reportDto, writer);
        }
        else if (reportDto instanceof OrderInPdf) {
            pdfDocumentAppender = new OrderPdf(reportDto, writer);
        }
        else if (reportDto instanceof ToBeShippedInPdf) {
            pdfDocumentAppender = new ToBeShippedPdf(reportDto);
        }
        else if (reportDto instanceof ToBeShippedToOneCustomerInPdf) {
            pdfDocumentAppender = new ToBeShippedToOneCustomerPdf(reportDto);
        }
        return pdfDocumentAppender;
    }

}
