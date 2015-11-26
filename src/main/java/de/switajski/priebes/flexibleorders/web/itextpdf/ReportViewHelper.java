package de.switajski.priebes.flexibleorders.web.itextpdf;

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

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.dto.ReportItemInPdf;

public class ReportViewHelper {

    public static List<Element> createAddress(Address adresse) throws DocumentException {
        return createAddress(adresse, null);
    }

    public static List<Element> createAddress(Address adresse, Image image)
            throws DocumentException {
        List<Element> elements = new ArrayList<Element>();

        float indentationLeftForAddress = 36f;
        elements.add(ParagraphBuilder.createEmptyLine());
        elements.add(ParagraphBuilder.createEmptyLine());
        elements.add(ParagraphBuilder.createEmptyLine());
        if (image != null) {
            Image img = Image.getInstance(image);
            img.setAlignment(Image.LEFT);
            img.setIndentationLeft(indentationLeftForAddress);
            img.scaleToFit(50, 20);
            elements.add(img);
        }
        else {
            elements.add(ParagraphBuilder.createEmptyLine());
        }

        if (adresse == null) {
            elements.add(ParagraphBuilder.createEmptyLine());
            elements.add(ParagraphBuilder.createEmptyLine());
            elements.add(ParagraphBuilder.createEmptyLine());
        }
        else {
            boolean name2Empty = StringUtils.isEmpty(adresse.getName2());

            ParagraphBuilder paragraphBuilder = new ParagraphBuilder(adresse.getName1())
                    .withIndentationLeft(indentationLeftForAddress)
                    .withLineSpacing(12f);
            if (!name2Empty) paragraphBuilder.addTextLine(adresse.getName2());
            paragraphBuilder.addTextLine(adresse.getStreet())
                    .addTextLine(adresse.getPostalCode() + " " + adresse.getCity())
                    .addTextLine(adresse.getCountry().getName());
            if (name2Empty) paragraphBuilder.addTextLine("");

            elements.add(paragraphBuilder.build());
        }
        elements.add(ParagraphBuilder.createEmptyLine());
        return elements;
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
