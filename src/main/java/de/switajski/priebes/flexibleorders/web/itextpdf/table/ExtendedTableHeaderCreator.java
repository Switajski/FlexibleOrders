package de.switajski.priebes.flexibleorders.web.itextpdf.table;

import static com.itextpdf.text.Chunk.NEWLINE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;
import de.switajski.priebes.flexibleorders.reference.DeliveryType;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;
import de.switajski.priebes.flexibleorders.web.itextpdf.shorthand.PdfPCellUtility;

public class ExtendedTableHeaderCreator {

    public PdfPTable create(ExtInfoTableParameter p) throws DocumentException {
        PdfPTable extInfoTable = new CustomPdfPTableBuilder(
                PdfPTableBuilder.createPropertiesWithThreeCols()).build();

        extInfoTable.addCell(createCellFor1stColumn(p));
        extInfoTable.addCell(createCellFor2ndColumn(p));
        extInfoTable.addCell(createCellFor3rdColumn(p));
        extInfoTable.setWidthPercentage(100);

        return extInfoTable;
    }

    private PdfPCell createCellFor1stColumn(ExtInfoTableParameter p) throws DocumentException {
        int columnSize = 2;
        PdfPTable table = new PdfPTable(columnSize);
        table.setWidths(new float[] { 22, 78 });
        appendDocNumbersIfNotEmpty(p.orderNumbers, table, "B-Nr.");
        appendDocNumbersIfNotEmpty(p.orderConfirmationNumbers, table, "AB-Nr.");
        appendDocNumbersIfNotEmpty(p.deliveryNotesNumbers, table, "L-Nr.");
        appendDocNumbersIfNotEmpty(p.invoiceNumbers, table, "R-Nr.");
        appendDocNumbersIfNotEmpty(p.creditNoteNumbers, table, "G-Nr.");

        table.addCell(colspan2(cellWithSmallFont(" ")));

        if (p.expectedDelivery != null) {
            table.addCell(colspan2(cellWithNormalFont(p.expectedDelivery)));
        }
        if (!isEmpty(p.mark)) {
            table.addCell(colspan2(cellWithNormalFont("Ihr Zeichen: ")));
            table.addCell(colspan2(cellWithSmallFont(p.mark)));
        }
        if (!isEmpty(p.saleRepresentative)) {
            table.addCell(colspan2(cellWithNormalFont("Vertreter: ")));
            table.addCell(colspan2(cellWithSmallFont(p.saleRepresentative)));
        }

        return wrapInCell(table);
    }

    private PdfPCell colspan2(PdfPCell cellWithNormalFont) {
        cellWithNormalFont.setColspan(2);
        return cellWithNormalFont;
    }

    private PdfPCell createCellFor2ndColumn(ExtInfoTableParameter p) {
        PdfPTable table = new PdfPTable(1);

        if (p.shippingAddress != null) {
            table.addCell(cellWithNormalFont("Lieferadresse:"));
            table.addCell(cellWithSmallFont(createString(p.shippingAddress)));
            if (p.deliveryMethod != null) {
                for (PdfPCell cell : createCells(p.deliveryMethod)) {
                    table.addCell(cell);
                }
            }
        }
        else {
            table.addCell(PdfPCellUtility.noBorder());
        }

        return wrapInCell(table);
    }

    private PdfPCell cellWithSmallFont(String createString) {
        return wrapInCell(new PhraseBuilder(createString).size8().build());
    }

    private PdfPCell cellWithNormalFont(String text) {
        return wrapInCell(new PhraseBuilder(text).build());
    }

    private PdfPCell wrapInCell(Phrase build) {
        PdfPCell cell = new PdfPCell(build);
        PdfPCellUtility.noBorder(cell);
        return cell;
    }

    private PdfPCell createCellFor3rdColumn(ExtInfoTableParameter p) {
        PdfPTable table = new PdfPTable(1);
        table.addCell(cellWithNormalFont("Kundennr.: " + p.customerNo));
        if (!isEmpty(p.vendorNumber)) {
            table.addCell(cellWithNormalFont("Lieferantennr.: " + p.vendorNumber));
        }
        if (p.contactInformation != null && !isEmpty(p.contactInformation.toString())) {
            table.addCell(cellWithNormalFont("Ihr Ansprechpartner:"));
            for (String str : createString(p.contactInformation))
                table.addCell(cellWithSmallFont(str));
        }

        return wrapInCell(table);
    }

    private PdfPCell wrapInCell(PdfPTable table) {
        PdfPCell pdfPCell = new PdfPCell(table);
        PdfPCellUtility.noBorder(pdfPCell);
        return pdfPCell;
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

    private List<PdfPCell> createCells(DeliveryMethod deliveryMethod) {
        List<PdfPCell> cells = new ArrayList<PdfPCell>();
        if (deliveryMethod.getDeliveryType() == DeliveryType.SPEDITION) {
            cells.add(cellWithNormalFont("Lieferart per Spedition:"));
            cells.add(cellWithSmallFont(createString(deliveryMethod.getAddress())));
        }
        else if (deliveryMethod.getDeliveryType() == DeliveryType.POST) {
            cells.add(cellWithNormalFont("Lieferart per Post"));
        }
        return cells;
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

    private List<String> createString(ContactInformation info) {
        List<String> strings = new ArrayList<String>();
        if (!isEmpty(info.getContact1())) strings.add(info.getContact1());
        if (!isEmpty(info.getContact2())) strings.add(info.getContact2());
        if (!isEmpty(info.getContact3())) strings.add(info.getContact3());
        if (!isEmpty(info.getContact4())) strings.add(info.getContact4());

        boolean inOneLine = true;
        if (inOneLine) {
            StringBuilder bld = new StringBuilder();
            Iterator<String> itr = strings.iterator();
            while (itr.hasNext()) {
                bld.append(itr.next());
                if (itr.hasNext()) bld.append("\n");
            }
            return Arrays.asList(bld.toString());
        }

        return strings;
    }

}
