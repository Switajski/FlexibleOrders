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
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.PdfConfiguration;
import de.switajski.priebes.flexibleorders.itextpdf.PdfDocumentAppender;
import de.switajski.priebes.flexibleorders.itextpdf.PdfDocumentAppenderFactory;
import de.switajski.priebes.flexibleorders.itextpdf.PdfTemplateFactory;
import de.switajski.priebes.flexibleorders.itextpdf.PdfUtils;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;

@Component
public class PdfView extends AbstractView {

    PdfConfiguration config;

    public PdfView() {
        setContentType("application/pdf");
        config = new PdfConfiguration();
    }

    @Override
    protected final void renderMergedOutputModel(
            Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response)
                    throws Exception {

        ReportInPdf report = (ReportInPdf) model.get(ReportInPdf.class.getSimpleName());

        // IE workaround: write into byte array first.
        ByteArrayOutputStream baos = createTemporaryOutputStream();

        // Apply preferences and build metadata.
        Document document = new PdfUtils().newDocument();
        PdfWriter writer = newWriter(document, baos);
        prepareWriter(report, writer, request);
        buildPdfMetadata(model, document, request);

        PdfDocumentAppender appender = new PdfDocumentAppenderFactory(writer).create(report);

        // Build PDF document.
        document.open();
        appender.append(document);
        document.close();

        // Flush to HTTP response.
        writeToResponse(response, baos);
    }

    protected void buildPdfMetadata(
            Map<String, Object> model,
            Document document,
            HttpServletRequest request) {}

    protected PdfWriter newWriter(Document document, OutputStream os)
            throws DocumentException {
        return PdfWriter.getInstance(document, os);
    }

    protected void prepareWriter(
            ReportInPdf report,
            PdfWriter writer,
            HttpServletRequest request) throws DocumentException, MalformedURLException, IOException {
        writer.setViewerPreferences(getViewerPreferences());
        writer.setPageEvent(new PdfTemplateFactory(config.logo()).create(report));
    }

    protected int getViewerPreferences() {
        return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
    }

}
