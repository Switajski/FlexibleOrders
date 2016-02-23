package de.switajski.priebes.flexibleorders.itextpdf;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEvent;

import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ToBeShippedDto;
import de.switajski.priebes.flexibleorders.itextpdf.template.BusinessLetterPdfTemplate;

public class PdfTemplateFactory {

    private Image logo;

    public PdfTemplateFactory(Image logo) {
        this.logo = logo;
    }

    public PdfPageEvent create(ReportDto reportDto) {
        if (reportDto instanceof ToBeShippedDto) {
            return new BlankPdfTemplate();
        }
        return new BusinessLetterPdfTemplate(logo, reportDto.address);
    }
}
