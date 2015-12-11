package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.dto.DeliveryNotesDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.InvoiceDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderConfirmationDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ToBeShippedDto;

public class PdfDocumentAppenderFactory {

    PdfDocumentAppender pdfDocumentAppender;
    Image logo;

    public PdfDocumentAppenderFactory(ReportDto reportDto, Image logo, PdfWriter writer) {
        this.logo = logo;
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
    }

    public PdfDocumentAppender create() {
        return pdfDocumentAppender;
    }

}
