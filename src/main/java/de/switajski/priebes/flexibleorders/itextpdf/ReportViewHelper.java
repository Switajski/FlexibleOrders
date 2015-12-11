package de.switajski.priebes.flexibleorders.itextpdf;

import static de.switajski.priebes.flexibleorders.itextpdf.shorthand.PdfPCellUtility.emptyCell;
import static de.switajski.priebes.flexibleorders.itextpdf.shorthand.PdfPCellUtility.noBorder;
import static de.switajski.priebes.flexibleorders.itextpdf.shorthand.PdfPCellUtility.wrapInCell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ColumnFormat;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportItemInPdf;

public class ReportViewHelper {

    public static List<Element> createHeaderWithAddress(Address adresse, Image image)
            throws DocumentException {
        List<Element> elements = new ArrayList<Element>();

        PdfPTable table = new PdfPTableBuilder(tableProperties()).withHeader(false).build();
        if (image != null) {
            table.addCell(emptyCell());

            Image img = Image.getInstance(image);
            img.setAlignment(Image.LEFT);
            img.scaleToFit(50, 20);
            PdfPCell cell = new PdfPCell(img, false);
            cell.setFixedHeight(70f);
            noBorder(cell);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(cell);
        }

        if (adresse == null) {
            PdfPCell pdfPCell = new PdfPCell();
            pdfPCell.setFixedHeight(20f);
            table.addCell(pdfPCell);
            table.addCell(emptyCell());
        }
        else {
            table.addCell(emptyCell());
            ParagraphBuilder paragraphBuilder = new ParagraphBuilder(adresse.getName1())
                    .withLineSpacing(12f);
            if (!StringUtils.isEmpty(adresse.getName2())) {
                paragraphBuilder.addTextLine(adresse.getName2());
            }
            paragraphBuilder.addTextLine(adresse.getStreet())
                    .addTextLine(adresse.getPostalCode() + " " + adresse.getCity())
                    .addTextLine(adresse.getCountry().getName());

            PdfPCell cell = wrapInCell(paragraphBuilder.build());
            cell.setFixedHeight(70f);
            table.addCell(cell);
        }
        elements.add(table);
        // TODO: return Table
        return elements;
    }

    private static ArrayList<ColumnFormat> tableProperties() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat("", Element.ALIGN_LEFT, 8));
        rowProperties.add(new ColumnFormat("", Element.ALIGN_LEFT, 100));
        return rowProperties;
    }

    public static List<Paragraph> createHeading(String heading)
            throws DocumentException {
        List<Paragraph> paragraphs = new ArrayList<Paragraph>();
        paragraphs.add(new ParagraphBuilder(heading).withFont(
                FontFactory.getFont(PriebesIText5PdfView.FONT, 12, Font.BOLD))
                .build());
        paragraphs.add(ParagraphBuilder.createEmptyLine());

        return paragraphs;
    }

    public static boolean hasManyCustomers(ReportDto report) {
        Set<String> customerNumbers = new HashSet<String>();
        for (ReportItemInPdf item : report.itemDtos) {
            customerNumbers.add(item.customerNumber);
        }
        return customerNumbers.size() > 1;
    }

    public static Paragraph createDate(String date) {
        return new ParagraphBuilder(date).withAlignment(Element.ALIGN_RIGHT)
                .build();
    }

    public static String createdDatesOf(Set<ShippingItem> sis) {
        String createdDates = "";
        Iterator<ShippingItem> itr = sis.iterator();
        while (itr.hasNext()) {
            ShippingItem si = itr.next();
            createdDates += PriebesIText5PdfView.dateFormat.format(si
                    .getReport().getCreated());
            if (itr.hasNext()) createdDates += " ";

        }
        return createdDates;
    }

    public static String documentNumbersOf(Set<ShippingItem> sis) {
        String documentNumbers = "";
        Iterator<ShippingItem> itr = sis.iterator();
        while (itr.hasNext()) {
            ShippingItem si = itr.next();
            documentNumbers += si.getReport().getDocumentNumber();
            if (itr.hasNext()) documentNumbers += ", ";

        }
        return documentNumbers;
    }
}
