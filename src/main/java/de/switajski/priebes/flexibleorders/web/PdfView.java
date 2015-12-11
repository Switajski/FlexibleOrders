package de.switajski.priebes.flexibleorders.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.BusinessLetterPdfTemplate;
import de.switajski.priebes.flexibleorders.itextpdf.PdfConfiguration;
import de.switajski.priebes.flexibleorders.itextpdf.PdfDocumentAppender;
import de.switajski.priebes.flexibleorders.itextpdf.PdfDocumentAppenderFactory;
import de.switajski.priebes.flexibleorders.itextpdf.PdfUtils;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;

@Component
public class PdfView extends AbstractView {

    Image logo;

    PdfConfiguration config;

    public PdfView() {
        setContentType("application/pdf");
        config = new PdfConfiguration();
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // IE workaround: write into byte array first.
        ByteArrayOutputStream baos = createTemporaryOutputStream();

        // Apply preferences and build metadata.
        Document document = new PdfUtils().newDocument();
        PdfWriter writer = newWriter(document, baos);
        prepareWriter(model, writer, request);
        buildPdfMetadata(model, document, request);

        // Choose ReportTemplate
        ReportDto report = (ReportDto) model.get(ReportDto.class.getSimpleName());
        PdfDocumentAppender appender = new PdfDocumentAppenderFactory(report, config.logo(), writer).create();

        // Build PDF document.
        document.open();
        appender.append(document);
        document.close();

        // Flush to HTTP response.
        writeToResponse(response, baos);
    }

    protected void buildPdfMetadata(Map<String, Object> model,
            Document document, HttpServletRequest request) {}

    protected PdfWriter newWriter(Document document, OutputStream os)
            throws DocumentException {
        return PdfWriter.getInstance(document, os);
    }

    protected void prepareWriter(Map<String, Object> model, PdfWriter writer,
            HttpServletRequest request) throws DocumentException, MalformedURLException, IOException {
        writer.setViewerPreferences(getViewerPreferences());
        writer.setPageEvent(new BusinessLetterPdfTemplate(config.logo()));
    }

    protected int getViewerPreferences() {
        return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
    }

}
