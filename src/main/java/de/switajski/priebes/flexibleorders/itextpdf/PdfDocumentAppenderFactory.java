package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.dto.DeliveryNotesDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.InvoiceDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderConfirmationDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ToBeShippedDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ToBeShippedToOneCustomerDto;

public class PdfDocumentAppenderFactory {

    private Image logo;
    private PdfWriter writer;

    public PdfDocumentAppenderFactory(Image logo, PdfWriter writer) {
        this.logo = logo;
        this.writer = writer;
    }

    public PdfDocumentAppender create(ReportDto reportDto) {
        PdfDocumentAppender pdfDocumentAppender = null;
        if (reportDto instanceof DeliveryNotesDto) {
            pdfDocumentAppender = new DeliveryNotesPdf(logo, reportDto);
        }
        else if (reportDto instanceof InvoiceDto) {
            pdfDocumentAppender = new InvoicePdf(logo, reportDto, writer);
        }
        else if (reportDto instanceof OrderConfirmationDto) {
            pdfDocumentAppender = new OrderConfirmationPdf(logo, reportDto, writer);
        }
        else if (reportDto instanceof OrderDto) {
            pdfDocumentAppender = new OrderPdf(logo, reportDto, writer);
        }
        else if (reportDto instanceof ToBeShippedDto) {
            pdfDocumentAppender = new ToBeShippedPdf(reportDto);
        }
        else if (reportDto instanceof ToBeShippedToOneCustomerDto) {
            pdfDocumentAppender = new ToBeShippedToOneCustomerPdf(logo, reportDto);
        }
        return pdfDocumentAppender;
    }

}
