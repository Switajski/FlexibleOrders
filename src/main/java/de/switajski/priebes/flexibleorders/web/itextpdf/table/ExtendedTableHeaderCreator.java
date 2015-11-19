package de.switajski.priebes.flexibleorders.web.itextpdf.table;

import static com.itextpdf.text.Chunk.NEWLINE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Collection;
import java.util.Iterator;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPCellBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;
import de.switajski.priebes.flexibleorders.reference.DeliveryType;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;
import de.switajski.priebes.flexibleorders.web.itextpdf.shorthand.PdfPCellUtility;

public class ExtendedTableHeaderCreator {

    public PdfPTable create(ExtInfoTableParameter p) throws DocumentException {
        PhraseBuilder pb = new PhraseBuilder("");
        PdfPCellBuilder cellb = new PdfPCellBuilder(new Phrase());

        int columnSize = 2;
        PdfPTable table = new PdfPTable(columnSize);
        appendDocNumbersIfNotEmpty(p.orderNumbers, table, "B-Nr.");
        appendDocNumbersIfNotEmpty(p.orderConfirmationNumbers, table, "AB-Nr.");
        appendDocNumbersIfNotEmpty(p.deliveryNotesNumbers, table, "L-Nr.");
        appendDocNumbersIfNotEmpty(p.invoiceNumbers, table, "R-Nr.");
        appendDocNumbersIfNotEmpty(p.creditNoteNumbers, table, "G-Nr.");
        table.setWidths(new float[] { 22, 78 });

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
        if (p.shippingAddress != null) {
            secondCol.addElement(new Phrase("Lieferadresse:"));
            secondCol.addElement(new PhraseBuilder(createString(p.shippingAddress)).size8().build());
            if (p.deliveryMethod != null) secondCol.addElement(new PhraseBuilder("\n" + createString(p.deliveryMethod)).size8().build());
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
            while (itr.hasNext()) {
                String s = itr.next();
                sb.append(s);
                if (itr.hasNext()) sb.append(", ");
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

}
