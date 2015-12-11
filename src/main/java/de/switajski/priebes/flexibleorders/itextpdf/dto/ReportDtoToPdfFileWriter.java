package de.switajski.priebes.flexibleorders.itextpdf.dto;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.CustomPdfTemplate;
import de.switajski.priebes.flexibleorders.itextpdf.PdfDocumentAppenderFactory;
import de.switajski.priebes.flexibleorders.itextpdf.PdfUtils;

@Component
public class ReportDtoToPdfFileWriter {

    public void writeFile(String pathAndFileName, Image logo, ReportDto reportDto) throws DocumentException, FileNotFoundException {
        Document document = new PdfUtils().newDocument();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pathAndFileName));
        pdfWriter.setPageEvent(new CustomPdfTemplate(logo));
        document.open();
        new PdfDocumentAppenderFactory(reportDto, logo, pdfWriter).create().append(document);
        document.close();
    }

}
