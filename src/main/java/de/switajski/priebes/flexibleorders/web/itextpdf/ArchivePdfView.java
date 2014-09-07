package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @author Marek Switajski
 * 
 */
@Component
public class ArchivePdfView extends PriebesIText5PdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    }

}
