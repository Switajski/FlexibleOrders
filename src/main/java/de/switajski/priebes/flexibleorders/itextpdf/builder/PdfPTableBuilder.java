package de.switajski.priebes.flexibleorders.itextpdf.builder;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.web.itextpdf.PriebesIText5PdfView;

public class PdfPTableBuilder {

    private List<List<String>> bodyList = new ArrayList<List<String>>();
    private List<String> footerList = new ArrayList<String>();
    private boolean createFooter = true;

    private ArrayList<ColumnFormat> tableProperties;
    private boolean createHeader = true;
    private Integer breakIndex;
    private String breakHeading;

    /**
     * Constructor with least arguments to build a {@link PdfPTable} table
     * 
     * @param rowProperties
     */
    public PdfPTableBuilder(ArrayList<ColumnFormat> rowProperties) {
        this.tableProperties = rowProperties;
    }

    public static ArrayList<ColumnFormat> createPropertiesWithFourCols() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties
                .add(new ColumnFormat("Anzahl", Element.ALIGN_RIGHT, 10));
        rowProperties.add(new ColumnFormat(
                "Art. Nr.",
                Element.ALIGN_LEFT,
                10));
        rowProperties
                .add(new ColumnFormat("Artikel", Element.ALIGN_LEFT, 50));
        rowProperties.add(new ColumnFormat(
                "Bestellnr.",
                Element.ALIGN_RIGHT,
                30));
        return rowProperties;
    }

    public static ArrayList<ColumnFormat> createPropertiesWithSixCols() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat(
                "Art. Nr.",
                Element.ALIGN_LEFT,
                10));
        rowProperties
                .add(new ColumnFormat("Artikel", Element.ALIGN_LEFT, 40));
        rowProperties
                .add(new ColumnFormat("Anzahl", Element.ALIGN_LEFT, 7));
        rowProperties.add(new ColumnFormat(
                "EK per St" + Unicode.U_UML + "ck",
                Element.ALIGN_LEFT,
                13));
        rowProperties.add(new ColumnFormat(
                "Bestellnr.",
                Element.ALIGN_LEFT,
                18,
                PriebesIText5PdfView.eightSizeFont));
        rowProperties.add(new ColumnFormat("gesamt", Element.ALIGN_RIGHT, 12));
        return rowProperties;
    }

    public static ArrayList<ColumnFormat> createPropertiesWithSevenCols() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat(
                "Art. Nr.",
                Element.ALIGN_LEFT,
                10));
        rowProperties
                .add(new ColumnFormat("Artikel", Element.ALIGN_LEFT, 25));
        rowProperties
                .add(new ColumnFormat("Anzahl", Element.ALIGN_RIGHT, 7));
        rowProperties.add(new ColumnFormat(
                "EK per St" + Unicode.U_UML + "ck",
                Element.ALIGN_RIGHT,
                12));
        rowProperties.add(new ColumnFormat(
                "Lieferscheinnr.",
                Element.ALIGN_RIGHT,
                15));
        rowProperties.add(new ColumnFormat(
                "Lieferdatum",
                Element.ALIGN_RIGHT,
                15));
        rowProperties
                .add(new ColumnFormat("gesamt", Element.ALIGN_RIGHT, 16));
        return rowProperties;
    }

    public static ArrayList<ColumnFormat> createPropertiesWithFiveCols() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat(
                "Art. Nr.",
                Element.ALIGN_LEFT,
                10));
        rowProperties
                .add(new ColumnFormat("Artikel", Element.ALIGN_LEFT, 50));
        rowProperties
                .add(new ColumnFormat("Anzahl", Element.ALIGN_LEFT, 10));
        rowProperties.add(new ColumnFormat(
                "EK per St\u00fcck",
                Element.ALIGN_LEFT,
                15));
        rowProperties
                .add(new ColumnFormat("gesamt", Element.ALIGN_RIGHT, 15));
        return rowProperties;
    }

    public static ArrayList<ColumnFormat> createPropertiesWithTwoCols() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat("1", Element.ALIGN_LEFT, 60));
        rowProperties.add(new ColumnFormat("2", Element.ALIGN_RIGHT, 40));
        return rowProperties;
    }

    public static ArrayList<ColumnFormat> createPropertiesWithThreeCols() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat("1", Element.ALIGN_LEFT, 33));
        rowProperties.add(new ColumnFormat("2", Element.ALIGN_LEFT, 33));
        rowProperties.add(new ColumnFormat("3", Element.ALIGN_LEFT, 34));
        return rowProperties;
    }

    private float[] getWidths() {
        ArrayList<Float> iList = new ArrayList<Float>();
        for (ColumnFormat prop : tableProperties) {
            iList.add(prop.relativeWidth);
        }
        return convertFloats(iList);
    }

    public static float[] convertFloats(List<Float> floats) {
        float[] ret = new float[floats.size()];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = floats.get(i).intValue();
        }
        return ret;
    }

    public PdfPTable build() throws DocumentException {
        PdfPTable pdfPTable = new PdfPTable(tableProperties.size());

        pdfPTable.setWidths(getWidths());
        pdfPTable.setWidthPercentage(100f);
        // if (pdfPTable.getTotalWidth() == 0);
        // pdfPTable.setTotalWidth(583f);

        if (createHeader) createHeader(pdfPTable);
        createBody(pdfPTable);
        if (createFooter) createFooter(pdfPTable);
        return pdfPTable;
    }

    private void createFooter(PdfPTable pdfPTable) {
        for (PdfPCell cell : createFooter(footerList, tableProperties)) {
            pdfPTable.addCell(cell);
        }
    }

    private void createHeader(PdfPTable pdfPTable) {
        for (PdfPCell cell : createHeaderCells()) {
            pdfPTable.addCell(cell);
        }
        pdfPTable.setHeaderRows(1);
    }

    public static List<PdfPCell> createFooter(List<String> footerList,
            ArrayList<ColumnFormat> tableProperties) {
        int current = 0;
        int last = footerList.size() - 1;
        List<PdfPCell> cells = new ArrayList<PdfPCell>();

        for (String string : footerList) {
            Phrase phrase = new Phrase(string,
                    FontFactory.getFont(
                            PriebesIText5PdfView.FONT,
                            PriebesIText5PdfView.FONT_SIZE,
                            Font.BOLD));
            PdfPCellBuilder builder = new PdfPCellBuilder(phrase)
                    .setColspan(tableProperties.size())
                    .withRightHorizontalAlignment();
            if (current == last) {
                builder.setTopBorder();
            }
            cells.add(builder.build());
            current++;
        }

        return cells;
    }

    private void createBody(PdfPTable pdfPTable) {
        for (int i = 0; i < bodyList.size(); i++) {
            if (breakIndex != null && breakIndex == i) {
                Phrase phrase = new PhraseBuilder(breakHeading).size12().build();
                PdfPCell cell = new PdfPCell();
                cell.addElement(phrase);
                cell.setBorder(Rectangle.BOTTOM);
                cell.setColspan(tableProperties.size());
                pdfPTable.addCell(cell);
            }

            List<String> bp = bodyList.get(i);
            int j = 0;
            for (String stringOfCell : bp) {
                pdfPTable.addCell(tableProperties.get(j).createCell(stringOfCell));
                j++;
            }
        }

    }

    public ArrayList<PdfPCell> createHeaderCells() {
        ArrayList<PdfPCell> header = new ArrayList<PdfPCell>();
        for (ColumnFormat prop : tableProperties) {
            PdfPCell bposHeader = new PdfPCell(
                    new PhraseBuilder(prop.heading).size8()
                            .build());
            bposHeader.setFixedHeight(12f);
            bposHeader.setHorizontalAlignment(prop.alignment);
            bposHeader.setBorder(Rectangle.TOP);
            bposHeader.setBorderWidth(PriebesIText5PdfView.BORDER_WIDTH);
            header.add(bposHeader);
        }
        return header;
    }

    /**
     * ArrayList must have same size as columns (given by
     * {@link PdfPTableBuilder})
     * 
     * @param list
     * @return
     */
    public PdfPTableBuilder addBodyRow(List<String> list) {
        if (list.size() != this.tableProperties.size()) throw new IllegalArgumentException(
                "Row number trying to add does not fit to table");
        bodyList.add(list);
        return this;
    }

    public PdfPTableBuilder addFooterRow(String string) {
        footerList.add(string);
        return this;
    }

    public PdfPTableBuilder withFooter(boolean createFooter) {
        this.createFooter = createFooter;
        return this;
    }

    public PdfPTableBuilder withHeader(boolean b) {
        createHeader = b;
        return this;
    }

    public void addBreak(String heading) {
        breakIndex = bodyList.size();
        breakHeading = heading;
    }

}
