package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.dto.DeliveryNotesDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.InvoiceDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderConfirmationDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ToBeShippedDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ToBeShippedToOneCustomerDto;

public class PdfDocumentAppenderFactory {

    private PdfWriter writer;

    public PdfDocumentAppenderFactory(PdfWriter writer) {
        this.writer = writer;
    }

    public PdfDocumentAppender create(ReportDto reportDto) {
        PdfDocumentAppender pdfDocumentAppender = null;
        if (reportDto instanceof DeliveryNotesDto) {
            pdfDocumentAppender = new DeliveryNotesPdf(reportDto);
        }
        else if (reportDto instanceof InvoiceDto) {
            pdfDocumentAppender = new InvoicePdf(reportDto, writer);
        }
        else if (reportDto instanceof OrderConfirmationDto) {
            pdfDocumentAppender = new OrderConfirmationPdf(reportDto, writer);
        }
        else if (reportDto instanceof OrderDto) {
            pdfDocumentAppender = new OrderPdf(reportDto, writer);
        }
        else if (reportDto instanceof ToBeShippedDto) {
            pdfDocumentAppender = new ToBeShippedPdf(reportDto);
        }
        else if (reportDto instanceof ToBeShippedToOneCustomerDto) {
            pdfDocumentAppender = new ToBeShippedToOneCustomerPdf(reportDto);
        }
        return pdfDocumentAppender;
    }

}
