package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.SimpleTableHeaderCreator;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.TableForPendingItemsCreator;

@Component
public class ToBeShippedPdfView extends PriebesIText5PdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ReportDto report = (ReportDto) model.get(ReportDto.class.getSimpleName());

        for (Paragraph p : ReportViewHelper.createHeading("Ausstehende Artikel"))
            document.add(p);

        String stringWithoutNull = toStringWithoutNull(report.customerNumber);
        if (!stringWithoutNull.equals("")) stringWithoutNull = "Kundennr.: " + stringWithoutNull;
        document.add(new SimpleTableHeaderCreator().create(
                toStringWithoutNull(report.customerFirstName) + " " + toStringWithoutNull(report.customerLastName),
                stringWithoutNull, "", ""));

        document.add(ParagraphBuilder.createEmptyLine());

        // insert main table
        document.add(new TableForPendingItemsCreator().create(report));

    }

    private String toStringWithoutNull(Object string) {
        if (string == null) return "";
        else return string.toString();
    }

    @Override
    public void insertBigLogo(PdfWriter writer) {

    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        if (SHOW_PAGE_NUMBERS) {
            addPageNumber(writer, document);
        }
    }

    /**
     * sets format and margins of document
     *
     * @return
     */
    protected Document newDocument() {
        Document doc = new Document(PageSize.A4);
        resetPageNumber();

        int PAGE_MARGIN_BOTTOM = /* bottom */70;
        int PAGE_MARGIN_TOP = /* top */60;
        int PAGE_MARGIN_RIGHT = /* right */72;
        int PAGE_MARGIN_LEFT = /* left */60;

        doc.setMargins(
                PAGE_MARGIN_LEFT,
                PAGE_MARGIN_RIGHT,
                PAGE_MARGIN_TOP,
                PAGE_MARGIN_BOTTOM);
        return doc;
    }

}
