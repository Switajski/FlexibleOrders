package de.switajski.priebes.flexibleorders.web.itextpdf.table;

import static com.itextpdf.text.Chunk.NEWLINE;
import static de.switajski.priebes.flexibleorders.web.itextpdf.shorthand.PdfPCellUtility.cellWithMixedFont;
import static de.switajski.priebes.flexibleorders.web.itextpdf.shorthand.PdfPCellUtility.cellWithSmallBoldFont;
import static de.switajski.priebes.flexibleorders.web.itextpdf.shorthand.PdfPCellUtility.cellWithSmallFont;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ColumnFormat;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;
import de.switajski.priebes.flexibleorders.reference.DeliveryType;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;
import de.switajski.priebes.flexibleorders.web.itextpdf.shorthand.PdfPCellUtility;

public class ExtendedTableHeaderCreator {

    public PdfPTable create(ExtInfoTableParameter p) throws DocumentException {
        PdfPTable extInfoTable = new CustomPdfPTableBuilder(
                tableProperties()).build();

        extInfoTable.addCell(createCellFor1stColumn(p));
        extInfoTable.addCell(createCellFor2ndColumn(p));
        extInfoTable.addCell(createCellFor3rdColumn(p));
        extInfoTable.setWidthPercentage(100);

        return extInfoTable;
    }

    private PdfPCell createCellFor1stColumn(ExtInfoTableParameter p) throws DocumentException {
        int columnSize = 2;
        PdfPTable table = new PdfPTable(columnSize);
        table.setWidths(new float[] { 23, 77 });
        appendDocNumbersIfNotEmpty(p.orderNumbers, table, "B-Nr.");
        appendDocNumbersIfNotEmpty(p.orderConfirmationNumbers, table, "AB-Nr.");
        appendDocNumbersIfNotEmpty(p.deliveryNotesNumbers, table, "L-Nr.");
        appendDocNumbersIfNotEmpty(p.invoiceNumbers, table, "R-Nr.");
        appendDocNumbersIfNotEmpty(p.creditNoteNumbers, table, "G-Nr.");
        if (p.packageNumber != null) {
            table.addCell(colspan2(cellWithMixedFont("Paketnr.: ", p.packageNumber)));
        }
        if (p.trackNumber != null) {
            table.addCell(colspan2(cellWithMixedFont("Sendungsnr.: ", p.trackNumber)));
        }

        table.addCell(colspan2(cellWithSmallFont(" ")));

        if (p.expectedDelivery != null) {
            table.addCell(colspan2(cellWithSmallFont(p.expectedDelivery)));
        }
        if (!isEmpty(p.mark)) {
            table.addCell(colspan2(cellWithMixedFont("Ihr Zeichen: ", p.mark)));
        }
        if (!isEmpty(p.saleRepresentative)) {
            table.addCell(colspan2(cellWithMixedFont("Vertreter: ", p.saleRepresentative)));
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
            table.addCell(cellWithSmallBoldFont("Lieferadresse:"));
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

    private PdfPCell createCellFor3rdColumn(ExtInfoTableParameter p) {
        PdfPTable table = new PdfPTable(1);
        table.addCell(alignRight(cellWithMixedFont("Kundennr.: ", p.customerNo)));
        if (!isEmpty(p.vendorNumber)) {
            table.addCell(alignRight(cellWithMixedFont("Lieferantennr.: ", p.vendorNumber)));
        }
        if (p.contactInformation != null && !isEmpty(p.contactInformation.toString())) {
            table.addCell(alignRight(cellWithSmallBoldFont("Ihr Ansprechpartner:")));
            for (String str : createString(p.contactInformation))
                table.addCell(alignRight(cellWithSmallFont(str)));
        }

        return wrapInCell(table);
    }

    private PdfPCell alignRight(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private PdfPCell wrapInCell(PdfPTable table) {
        PdfPCell pdfPCell = new PdfPCell(table);
        PdfPCellUtility.noBorder(pdfPCell);
        return pdfPCell;
    }

    private static void appendDocNumbersIfNotEmpty(Collection<String> strings,
            PdfPTable table, String docNoName) {
        if (strings != null && !strings.isEmpty()) {
            PdfPCell cell = new PdfPCell(new PhraseBuilder()
                    .size8Bold().append(docNoName).append(":").build());
            PdfPCellUtility.noBorder(cell);
            table.addCell(cell);

            StringBuilder sb = new StringBuilder();
            Iterator<String> itr = strings.iterator();
            while (itr.hasNext()) {
                String s = itr.next();
                sb.append(s);
                if (itr.hasNext()) sb.append(", ");
            }
            PdfPCell cell2 = new PdfPCell(new PhraseBuilder()
                    .size8().append(sb.toString()).build());
            PdfPCellUtility.noBorder(cell2);
            table.addCell(cell2);
        }
    }

    private List<PdfPCell> createCells(DeliveryMethod deliveryMethod) {
        List<PdfPCell> cells = new ArrayList<PdfPCell>();
        if (deliveryMethod.getDeliveryType() == DeliveryType.SPEDITION) {
            cells.add(cellWithMixedFont("Lieferart: ", "Spedition"));
        }
        else if (deliveryMethod.getDeliveryType() == DeliveryType.POST) {
            cells.add(cellWithMixedFont("Lieferart: ", "Post"));
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

    public ArrayList<ColumnFormat> tableProperties() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat("1", Element.ALIGN_LEFT, 33));
        rowProperties.add(new ColumnFormat("2", Element.ALIGN_LEFT, 33));
        rowProperties.add(new ColumnFormat("3", Element.ALIGN_LEFT, 34));
        return rowProperties;
    }

}
