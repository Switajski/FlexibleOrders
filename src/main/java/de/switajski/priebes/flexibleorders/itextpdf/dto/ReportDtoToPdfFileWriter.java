package de.switajski.priebes.flexibleorders.itextpdf.dto;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.PdfDocumentAppenderFactory;
import de.switajski.priebes.flexibleorders.itextpdf.PdfUtils;
import de.switajski.priebes.flexibleorders.itextpdf.template.BusinessLetterPdfTemplate;

@Component
public class ReportDtoToPdfFileWriter {

    public void writeFile(String pathAndFileName, Image logo, ReportInPdf reportDto) throws DocumentException, FileNotFoundException {
        Document document = new PdfUtils().newDocument();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pathAndFileName));
        pdfWriter.setPageEvent(new BusinessLetterPdfTemplate(logo, reportDto.address, "Datum: 22.1.2016", "das Datum stimmt eigentlich nicht", "Betreff"));
        document.open();
        new PdfDocumentAppenderFactory(pdfWriter).create(reportDto).append(document);
        document.close();
    }

}
