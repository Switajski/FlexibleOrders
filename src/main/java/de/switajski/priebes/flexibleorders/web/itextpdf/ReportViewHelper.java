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
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPCellBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;
import de.switajski.priebes.flexibleorders.reference.DeliveryType;
import de.switajski.priebes.flexibleorders.service.helper.StringUtil;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;

public class ReportViewHelper {

    public static List<Paragraph> createAddress(Address adresse)
            throws DocumentException {
        List<Paragraph> paragraphs = new ArrayList<Paragraph>();

        paragraphs.add(ParagraphBuilder.createEmptyLine());
        paragraphs.add(ParagraphBuilder.createEmptyLine());
        paragraphs.add(ParagraphBuilder.createEmptyLine());
        paragraphs.add(ParagraphBuilder.createEmptyLine());
        if (adresse == null) {
            paragraphs.add(ParagraphBuilder.createEmptyLine());
            paragraphs.add(ParagraphBuilder.createEmptyLine());
            paragraphs.add(ParagraphBuilder.createEmptyLine());
        }
        else {
            paragraphs.add(new ParagraphBuilder(adresse.getName1())
                    .withIndentationLeft(36f)
                    .withLineSpacing(12f)
                    .addTextLine(adresse.getName2())
                    .addTextLine(adresse.getStreet())
                    .addTextLine(
                            adresse.getPostalCode() + " " + adresse.getCity())
                    .addTextLine(adresse.getCountry().toString())
                    .build());
        }
        paragraphs.add(ParagraphBuilder.createEmptyLine());
        return paragraphs;
    }

    public static PdfPTable createInfoTable(String rightTop,
            String rightBottom, String leftTop, String leftBottom)
            throws DocumentException {

        CustomPdfPTableBuilder infoTableBuilder = CustomPdfPTableBuilder
                .createInfoTable(
                        leftTop, leftBottom, rightTop, rightBottom);
        PdfPTable infoTable = infoTableBuilder.build();
        infoTable.setWidthPercentage(100);

        return infoTable;
    }

    public static List<Paragraph> createHeading(String heading)
            throws DocumentException {
        List<Paragraph> paragraphs = new ArrayList<Paragraph>();
        paragraphs.add(new ParagraphBuilder(heading)
                .withFont(
                        FontFactory.getFont(
                                PriebesIText5PdfView.FONT,
                                12,
                                Font.BOLD))
                .build());
        paragraphs.add(ParagraphBuilder.createEmptyLine());

        return paragraphs;
    }

    public static PdfPTable createExtInfoTable(ExtInfoTableParameter p) {
        PhraseBuilder pb = new PhraseBuilder("");
        PdfPCellBuilder cellb = new PdfPCellBuilder(new Phrase());

        String shipAddress = createString(p.agreementDetails.getShippingAddress());

        Phrase firstCol = new PhraseBuilder().append(createString(p.orderNumbers, "B-Nr.")).build();
        appendDocNumbersIfNotEmpty(p.orderConfirmationNumbers, firstCol, "AB-Nr.");
        appendDocNumbersIfNotEmpty(p.deliveryNotesNumbers, firstCol, "L-Nr.");
        appendDocNumbersIfNotEmpty(p.invoiceNumbers, firstCol, "R-Nr.");
        appendDocNumbersIfNotEmpty(p.creditNoteNumbers, firstCol, "Gutschirft-Nr.");

        firstCol.add(NEWLINE);
        if (p.expectedDelivery != null)
            firstCol.add(new PhraseBuilder().append(NEWLINE + "" + p.expectedDelivery).build());
        firstCol.add(
                new PhraseBuilder()
                        .append(NEWLINE)
                        .append(NEWLINE).append(isEmpty(p.customerDetails.getMark()) ? "" : "Ihr Zeichen: " + NEWLINE + p.customerDetails.getMark())
                        .append(
                                StringUtils.isEmpty(p.customerDetails.getSaleRepresentative()) ? "" : NEWLINE + "" + NEWLINE + "Vertreter: " + NEWLINE
                                        + p.customerDetails.getSaleRepresentative())
                        .build()
                );

        String sdCol = new StringBuilder()
                .append("Lieferadresse:")
                .append(NEWLINE + shipAddress)
                .append(NEWLINE)
                .append(NEWLINE)
                .append(createString(p.agreementDetails.getDeliveryMethod()))
                .toString();

        String rdCol = new StringBuilder()
                .append(isEmpty(p.customerDetails.getVendorNumber()) ? "" : "Lieferantennr.: " + p.customerDetails.getVendorNumber() + NEWLINE + NEWLINE)
                .append(createString(p.customerDetails.getContactInformation()))
                .toString();

        CustomPdfPTableBuilder infoTableBuilder = new CustomPdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithThreeCols())
                .addCell(cellb.withPhrase(firstCol).build())
                .addCell(cellb.withPhrase(
                        pb.withText(sdCol).build()).build())
                .addCell(cellb.withPhrase(
                        pb.withText(rdCol).build()).build());

        PdfPTable infoTable = infoTableBuilder.build();

        infoTable.setWidthPercentage(100);

        return infoTable;
    }

    private static void appendDocNumbersIfNotEmpty(Collection<String> p, Phrase firstCol, String docNoName) {
        if (p != null && p.size() > 0) firstCol.add(new PhraseBuilder()
                .append(NEWLINE + createString(p, docNoName)).build());
    }

    private static String createString(DeliveryMethod deliveryMethod) {
        if (deliveryMethod == null) return null;
        if (deliveryMethod.getDeliveryType() == DeliveryType.SPEDITION) {
            return new StringBuilder()
                    .append("Lieferart per Spedition:")
                    .append(NEWLINE + createString(deliveryMethod.getAddress())).toString();
        }
        else if (deliveryMethod.getDeliveryType() == DeliveryType.POST) {
            return new StringBuilder()
                    .append("Lieferart per Post").toString();
        }
        return "";
    }

    private static String createString(Collection<String> documentNumbers, String singular) {
        return new StringBuilder().append(singular + ": ").append(StringUtil.concatWithCommas(documentNumbers)).toString();
    }

    private static String createString(Address a) {
        StringBuilder cib = new StringBuilder();
        if (a != null) {
            if (!isEmpty(a.getName1())) cib.append(a.getName1());
            if (!isEmpty(a.getName2())) cib.append(a.getName2());

            if (!isEmpty(a.getStreet())) cib.append(NEWLINE + a.getStreet());

            cib.append(NEWLINE + a.getPostalCode().toString() + " ");
            cib.append(a.getCity());

            if (!isEmpty(a.getCountry().toString())) cib.append(NEWLINE).append(a.getCountry());
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

    public static PdfPTable createExtendedTable(Report report)
            throws DocumentException {
        PdfPTableBuilder builder = new PdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithSixCols());
        for (ReportItem he : report.getItemsOrdered()) {
            List<String> list = new ArrayList<String>();
            // Art.Nr.:
            Long pNo = he.getOrderItem().getProduct().getProductNumber();
            list.add(pNo.equals(0L) ? "n.a." : pNo.toString());
            // Artikel
            list.add(he.getOrderItem().getProduct().getName());
            // Anzahl
            list.add(String.valueOf(he.getQuantity()));
            // EK per Stueck
            list.add(he.getOrderItem().getNegotiatedPriceNet().toString());
            // Bestellnr
            list.add(he.getOrderItem().getOrder().getOrderNumber());
            // gesamt
            list.add(he
                    .getOrderItem()
                    .getNegotiatedPriceNet()
                    .multiply(he.getQuantity())
                    .toString());

            builder.addBodyRow(list);
        }

        return builder.withFooter(false).build();
    }

    public static PdfPTable createTableWithoutPrices(Report cReport) throws DocumentException {
        PdfPTableBuilder builder = new PdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithFourCols());
        for (ReportItem he : cReport.getItemsOrdered()) {
            if (!he.getOrderItem().isShippingCosts()) {
                List<String> row = new ArrayList<String>();
                // Anzahl
                row.add(String.valueOf(he.getQuantity()) + " x ");
                // Art.Nr.:
                Long pNo = he.getOrderItem().getProduct().getProductNumber();
                row.add(pNo.equals(0L) ? "n.a." : pNo.toString());
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
        return new ParagraphBuilder(date).withAlignment(Element.ALIGN_RIGHT).build();
    }

    public static ExtInfoTableParameter mapDocumentNumbersToParam(
            DeliveryHistory history, ExtInfoTableParameter param) {
        param.orderNumbers = history.getOrderNumbers();
        param.invoiceNumbers = history.getInvoiceNumbers();
        param.orderAgreementNumbers = history.getOrderAgreementNumbers();
        param.deliveryNotesNumbers = history.getDeliveryNotesNumbers();
        param.creditNoteNumbers = history.getCreditNoteNumbers();
        return param;
    }
    
    public static String createdDatesOf(Set<ShippingItem> sis) {
        String createdDates = "";
        Iterator<ShippingItem> itr = sis.iterator();
        while (itr.hasNext()){
            ShippingItem si = itr.next();
            createdDates+= PriebesIText5PdfView.dateFormat.format(si.getReport().getCreated());
            if (itr.hasNext())
                createdDates += " ";
            
        }
        return createdDates;
    }
    
    public static String documentNumbersOf(Set<ShippingItem> sis) {
        String documentNumbers = "";
        Iterator<ShippingItem> itr = sis.iterator();
        while (itr.hasNext()){
            ShippingItem si = itr.next();
            documentNumbers+= si.getReport().getDocumentNumber();
            if (itr.hasNext())
                documentNumbers += ", ";
            
        }
        return documentNumbers;
    }
}
