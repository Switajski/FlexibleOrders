package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEvent;

import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ToBeShippedInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.template.BusinessLetterPdfTemplate;

public class PdfTemplateFactory {

    private Image logo;

    public PdfTemplateFactory(Image logo) {
        this.logo = logo;
    }

    public PdfPageEvent create(ReportInPdf reportDto) {
        if (reportDto instanceof ToBeShippedInPdf) {
            return new BlankPdfTemplate();
        }
        return new BusinessLetterPdfTemplate(
                logo,
                reportDto.address,
                reportDto.date,
                reportDto.noteOnDate,
                reportDto.subject);
    }
}
