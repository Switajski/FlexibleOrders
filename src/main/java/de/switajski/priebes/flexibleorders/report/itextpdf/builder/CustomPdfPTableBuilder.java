package de.switajski.priebes.flexibleorders.report.itextpdf.builder;

import java.util.ArrayList;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.report.itextpdf.PriebesIText5PdfView;

//TODO make this default the default builder and let PdfPTableBuilder extend this 
public class CustomPdfPTableBuilder {

	public ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();
	
	private ArrayList<TableProperties> tableProperties;

	private Float totalWidth = null;
	
	public CustomPdfPTableBuilder(
			ArrayList<TableProperties> tableProperties) {
		this.tableProperties = tableProperties;
	}

	public PdfPTable build(){
		PdfPTable pdfPTable = new PdfPTable(tableProperties.size());
		for (PdfPCell cell:cells)
			pdfPTable.addCell(cell);
		if (totalWidth != null)
			pdfPTable.setTotalWidth(totalWidth);
		return pdfPTable;
	}
	
	public CustomPdfPTableBuilder addCell(PdfPCell cell){
		this.cells.add(cell);
		return this;
	}
	
	public CustomPdfPTableBuilder withTotalWidth(Float totalWidth){
		this.totalWidth = totalWidth;
		return this;
	}
	
	/**
	 * Create a pre-configured table builder for footer 
	 * @param net
	 * @param vat
	 * @return
	 */
	public static CustomPdfPTableBuilder createFooterBuilder(Amount net, Amount vat) {
		PhraseBuilder bold = new PhraseBuilder("Betrag").withFont(FontFactory.getFont(PriebesIText5PdfView.FONT, PriebesIText5PdfView.FONT_SIZE, Font.BOLD));
		PdfPCellBuilder leftAlign = new PdfPCellBuilder(bold.build());
		PdfPCellBuilder rightAlign = new PdfPCellBuilder(bold.build()).withRightHorizontalAlignment();
		
		CustomPdfPTableBuilder footerBuilder = new CustomPdfPTableBuilder(PdfPTableBuilder.createPropertiesWithTwoCols())
        .addCell(leftAlign.withPhrase(bold.withText("Betrag").build()).build())
        .addCell(rightAlign.withPhrase(bold.withText(net.toString()).build()).build())
        
        .addCell(leftAlign.withPhrase(bold.withText("zzgl. 19% MwSt.").build()).build())
        .addCell(rightAlign.withPhrase(bold.withText(vat.toString()).build()).build())
        
        .addCell(leftAlign.withPhrase(bold.withText("Gesamtbetrag brutto").build()).withBorder(Rectangle.TOP).build())
        .addCell(rightAlign.withPhrase(bold.withText(net.add(vat).toString()).build()).withBorder(Rectangle.TOP).build());

		return footerBuilder;
	}

	public static CustomPdfPTableBuilder createInfoTable(String documentNumber, String created,
			String expectedDelivery, String customerNumber) {
		if (expectedDelivery != null &&  expectedDelivery != "")
			expectedDelivery = "vorraussichtl. Liefertermin: "+expectedDelivery;
		else expectedDelivery = "";
		
		if (customerNumber != null &&  customerNumber != "")
			customerNumber = "Kundennr.: "+customerNumber;
		else customerNumber = "";
		
		CustomPdfPTableBuilder infoTableBuilder = new CustomPdfPTableBuilder(PdfPTableBuilder.createPropertiesWithTwoCols())
    	.addCell(new PdfPCellBuilder(
    			new PhraseBuilder("Auftragsnummer: "  + documentNumber).build()).build())
    	.addCell(new PdfPCellBuilder(
    			new PhraseBuilder(expectedDelivery).build()).build())
    	.addCell(new PdfPCellBuilder(
    			new PhraseBuilder("Auftragsdatum: "  + created).build()).build())
    	.addCell(new PdfPCellBuilder(
    			new PhraseBuilder(customerNumber).build()).build());
		return infoTableBuilder;
	}
}
