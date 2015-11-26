package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.SimpleTableHeaderCreator;
import de.switajski.priebes.flexibleorders.web.itextpdf.table.TableForPendingItemsCreator;

@Component
public class ToBeShippedToOneCustomerPdfView extends PriebesIText5PdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ReportDto report = (ReportDto) model.get(ReportDto.class.getSimpleName());

        for (Element p : ReportViewHelper.createAddress(report.shippingSpecific_shippingAddress, this.createLogo()))
            document.add(p);

        for (Paragraph p : ReportViewHelper.createHeading("Ausstehende Artikel"))
            document.add(p);

        String stringWithoutNull = toStringWithoutNull(report.customerNumber);
        if (!stringWithoutNull.equals("")) stringWithoutNull = "Kundennr.: " + stringWithoutNull;
        document.add(new SimpleTableHeaderCreator().create("", "",
                report.customerFirstName + " " + toStringWithoutNull(report.customerLastName),
                stringWithoutNull));

        document.add(ParagraphBuilder.createEmptyLine());

        // insert main table
        document.add(new TableForPendingItemsCreator().create(report));

    }

    private String toStringWithoutNull(Object string) {
        if (string == null) return "";
        else return string.toString();
    }

}
