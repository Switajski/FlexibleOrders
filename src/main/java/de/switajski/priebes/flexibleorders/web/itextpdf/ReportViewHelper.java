package de.switajski.priebes.flexibleorders.web.itextpdf;

import static com.itextpdf.text.Chunk.NEWLINE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Collection;
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPCellBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;
import de.switajski.priebes.flexibleorders.reference.DeliveryType;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;
import de.switajski.priebes.flexibleorders.web.itextpdf.shorthand.PdfPCellUtility;

public class ReportViewHelper {
	
	public static List<Element> createAddress(Address adresse) throws DocumentException{
		return createAddress(adresse, null);
	}

    public static List<Element> createAddress(Address adresse, Image image)
            throws DocumentException {
        List<Element> elements = new ArrayList<Element>();

        float indentationLeftForAddress = 36f;
        elements.add(ParagraphBuilder.createEmptyLine());
        elements.add(ParagraphBuilder.createEmptyLine());
        elements.add(ParagraphBuilder.createEmptyLine());
		if (image != null){
        	Image img = Image.getInstance(image);
            img.setAlignment(Image.LEFT);
            img.setIndentationLeft(indentationLeftForAddress);
            img.scaleToFit(50, 20);
            elements.add(img);
        } else {
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
			if (name2Empty)
            	paragraphBuilder.addTextLine(adresse.getName2());
			paragraphBuilder.addTextLine(adresse.getStreet())
				.addTextLine(adresse.getPostalCode() + " " + adresse.getCity())
				.addTextLine(adresse.getCountry().getName());
			if (name2Empty)
				paragraphBuilder.addTextLine("");
            
			elements.add(paragraphBuilder.build());
        }
        elements.add(ParagraphBuilder.createEmptyLine());
        return elements;
    }

    public static PdfPTable createInfoTable(String rightTop,
            String rightBottom, String leftTop, String leftBottom)
            throws DocumentException {

        CustomPdfPTableBuilder infoTableBuilder = CustomPdfPTableBuilder
                .createInfoTable(leftTop, leftBottom, rightTop, rightBottom);
        PdfPTable infoTable = infoTableBuilder.build();
        infoTable.setWidthPercentage(100);

        return infoTable;
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

    public static PdfPTable createExtInfoTable(ExtInfoTableParameter p) throws DocumentException {
        PhraseBuilder pb = new PhraseBuilder("");
        PdfPCellBuilder cellb = new PdfPCellBuilder(new Phrase());

        int columnSize = 2;
		PdfPTable table = new PdfPTable(columnSize);
        appendDocNumbersIfNotEmpty(p.orderNumbers, table, "B-Nr.");
        appendDocNumbersIfNotEmpty(p.orderConfirmationNumbers, table, "AB-Nr.");
        appendDocNumbersIfNotEmpty(p.deliveryNotesNumbers, table, "L-Nr.");
        appendDocNumbersIfNotEmpty(p.invoiceNumbers, table, "R-Nr.");
        appendDocNumbersIfNotEmpty(p.creditNoteNumbers, table, "G-Nr.");
        table.setWidths(new float[]{22, 78});
        

        Phrase firstCol = new PhraseBuilder().build();
        firstCol.add(NEWLINE);
        if (p.expectedDelivery != null) {
            firstCol.add(new PhraseBuilder().append(NEWLINE + "" + p.expectedDelivery).build());
        }
        firstCol.add(new PhraseBuilder()
                .append(isEmpty(p.mark) ? "" : "Ihr Zeichen: " + NEWLINE + p.mark)
                .append(isEmpty(p.saleRepresentative) ? "" :
                        NEWLINE + "" + NEWLINE + "Vertreter: " + NEWLINE + p.saleRepresentative).build());
        PdfPCell cell = new PdfPCell(firstCol);
        PdfPCellUtility.noBorder(cell);
        cell.setColspan(columnSize);
        table.addCell(cell);

        PdfPCell secondCol = new PdfPCell();
        PdfPCellUtility.noBorder(secondCol);
        if (p.shippingAddress != null){
            secondCol.addElement(new Phrase("Lieferadresse:"));
            secondCol.addElement(new PhraseBuilder(createString(p.shippingAddress)).size8().build());
            if (p.deliveryMethod != null)
                secondCol.addElement(new PhraseBuilder("\n" + createString(p.deliveryMethod)).size8().build());
        }

        String thirdColBuilder = new StringBuilder()
                .append("Kundennr.: " + p.customerNo + NEWLINE)
                .append(isEmpty(p.vendorNumber) ? "" : "Lieferantennr.: "
                        + p.vendorNumber + NEWLINE + NEWLINE)
                .append(createString(p.contactInformation))
                .toString();

        PdfPTable extInfoTable = new CustomPdfPTableBuilder(
        PdfPTableBuilder.createPropertiesWithThreeCols()).build();
        PdfPCell firstCell = new PdfPCell(table);
        PdfPCellUtility.noBorder(firstCell);
        extInfoTable.addCell(firstCell);
        extInfoTable.addCell(secondCol);
        extInfoTable.addCell(cellb.withPhrase(pb.withText(thirdColBuilder).build()).build());
        extInfoTable.setWidthPercentage(100);

        return extInfoTable;
    }

    private static void appendDocNumbersIfNotEmpty(Collection<String> strings,
            PdfPTable table, String docNoName) {
        if (strings != null && !strings.isEmpty()) {
        	PdfPCell cell = new PdfPCell(new PhraseBuilder().size8().append(docNoName).append(":").build());
        	PdfPCellUtility.noBorder(cell);
			table.addCell(cell);
        	
			StringBuilder sb = new StringBuilder();
        	Iterator<String> itr = strings.iterator();
        	while (itr.hasNext()){
        		String s = itr.next();
        		sb.append(s);
        		if (itr.hasNext())
        			sb.append(", ");
        	}
        	PdfPCell cell2 = new PdfPCell(new PhraseBuilder().size8().append(sb.toString()).build());
        	PdfPCellUtility.noBorder(cell2);
			table.addCell(cell2);
        }
    }

    private static String createString(DeliveryMethod deliveryMethod) {
        if (deliveryMethod == null) return "";
        if (deliveryMethod.getDeliveryType() == DeliveryType.SPEDITION) {
            return new StringBuilder()
                    .append("Lieferart per Spedition:")
                    .append(NEWLINE + createString(deliveryMethod.getAddress()))
                    .toString();
        }
        else if (deliveryMethod.getDeliveryType() == DeliveryType.POST) {
            return new StringBuilder().append("Lieferart per Post").toString();
        }
        return "";
    }

    private static String createString(Address a) {
        StringBuilder cib = new StringBuilder();
        if (a != null) {
            if (!isEmpty(a.getName1())) cib.append(a.getName1()).append(NEWLINE);
            if (!isEmpty(a.getName2())) cib.append(a.getName2()).append(NEWLINE);
            if (!isEmpty(a.getStreet())) cib.append(a.getStreet()).append(NEWLINE);

            cib.append(a.getPostalCode().toString() + " ");
            cib.append(a.getCity()).append(NEWLINE);

            if (!isEmpty(a.getCountry().getName())) cib.append(a.getCountry().getName());
        }
        return cib.toString();
    }

    private static String createString(ContactInformation info) {
        StringBuilder cib = new StringBuilder();
        if (info != null && !isEmpty(info.toString())) {
            cib.append("Ihr Ansprechpartner:");
            if (!isEmpty(info.getContact1())) cib.append(NEWLINE).append(info.getContact1());
            if (!isEmpty(info.getContact2())) cib.append(NEWLINE).append(info.getContact2());
            if (!isEmpty(info.getContact3())) cib.append(NEWLINE).append(info.getContact3());
            if (!isEmpty(info.getContact4())) cib.append(NEWLINE).append(info.getContact4());
        }
        String cinfo = cib.toString();
        return cinfo;
    }

    public static PdfPTable createExtendedTable(ReportDto report)
            throws DocumentException {
        PdfPTableBuilder builder = new PdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithSixCols());
        // Refactor - see #71
        for (ReportItem he : report.getItemsByOrder()) {
            List<String> list = new ArrayList<String>();
            // Art.Nr.:
            Product product = he.getOrderItem().getProduct();
            list.add(product.hasProductNo() ? product.getProductNumber() : "n.a.");
            // Artikel
            list.add(product.getName());
            // Anzahl
            list.add(String.valueOf(he.getQuantity()));
            // EK per Stueck
            list.add(he.getOrderItem().getNegotiatedPriceNet().toString());
            // Bestellnr
            list.add(he.getOrderItem().getOrder().getOrderNumber());
            // gesamt
            list.add(he.getOrderItem().getNegotiatedPriceNet()
                    .multiply(he.getQuantity()).toString());

            builder.addBodyRow(list);
        }

        return builder.withFooter(false).build();
    }

    public static PdfPTable createTableWithoutPrices(ReportDto cReport)
            throws DocumentException {
        PdfPTableBuilder builder = new PdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithFourCols());
        // Refactor - see #71
        for (ReportItem he : cReport.getItemsByOrder()) {
            if (!he.getOrderItem().isShippingCosts()) {
                List<String> row = new ArrayList<String>();
                // Anzahl
                row.add(String.valueOf(he.getQuantity()) + " x ");
                // Art.Nr.:
                String pNo = he.getOrderItem().getProduct().getProductNumber();
                row.add(pNo.equals("0") ? "n.a." : pNo.toString());
                // Artikel
                row.add(he.getOrderItem().getProduct().getName());
                // Bestellnr
                row.add(he.getOrderItem().getOrder().getOrderNumber());

                builder.addBodyRow(row);
            }
        }

        return builder.withFooter(false).build();
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
