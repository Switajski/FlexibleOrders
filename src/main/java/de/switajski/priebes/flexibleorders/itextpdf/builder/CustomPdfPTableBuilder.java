package de.switajski.priebes.flexibleorders.itextpdf.builder;

import java.util.ArrayList;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.web.itextpdf.PriebesIText5PdfView;

//TODO make this default the default builder and let PdfPTableBuilder extend this 
public class CustomPdfPTableBuilder {

	public ArrayList<PdfPCell> cells = new ArrayList<PdfPCell>();

	private ArrayList<TableProperties> tableProperties;

	private Float totalWidth = null;

	public CustomPdfPTableBuilder(
			ArrayList<TableProperties> tableProperties) {
		this.tableProperties = tableProperties;
	}

	public PdfPTable build() {
		PdfPTable pdfPTable = new PdfPTable(tableProperties.size());
		for (PdfPCell cell : cells)
			pdfPTable.addCell(cell);
		if (totalWidth != null)
			pdfPTable.setTotalWidth(totalWidth);
		return pdfPTable;
	}

	public CustomPdfPTableBuilder addCell(PdfPCell cell) {
		this.cells.add(cell);
		return this;
	}

	public CustomPdfPTableBuilder withTotalWidth(Float totalWidth) {
		this.totalWidth = totalWidth;
		return this;
	}

	/**
	 * Create a pre-configured table builder for footer
	 * 
	 * @param net
	 * @param vat
	 * @return
	 */
	public static CustomPdfPTableBuilder createFooterBuilder(Amount net,
			Amount vat, Amount shipping, Amount gross, String paymentConditions) {
		PhraseBuilder bold = new PhraseBuilder("Betrag").withFont(FontFactory
				.getFont(
						PriebesIText5PdfView.FONT,
						PriebesIText5PdfView.FONT_SIZE,
						Font.BOLD));
		PdfPCellBuilder leftAlign = new PdfPCellBuilder(bold.build());
		PdfPCellBuilder rightAlign = new PdfPCellBuilder(bold.build())
				.withRightHorizontalAlignment();

		CustomPdfPTableBuilder footerBuilder = new CustomPdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithTwoCols())
				.addCell(
						leftAlign
								.withPhrase(bold.withText("Betrag").build())
								.build())
				.addCell(
						rightAlign.withPhrase(
								bold.withText(net.toString()).build()).build());

		if (shipping != null)
			footerBuilder
					.addCell(
							leftAlign.withPhrase(
									bold.withText("Versand").build()).build())
					.addCell(
							rightAlign
									.withPhrase(
											bold
													.withText(
															shipping.toString())
													.build())
									.build());

		footerBuilder.addCell(
				leftAlign
						.withPhrase(bold.withText("zzgl. 19% MwSt.").build())
						.build())
				.addCell(
						rightAlign.withPhrase(
								bold.withText(vat.toString()).build()).build());

		footerBuilder
				.addCell(
						leftAlign
								.withPhrase(
										bold
												.withText("Gesamtbetrag brutto")
												.build())
								.withBorder(Rectangle.TOP)
								.build())
				.addCell(
						rightAlign
								.withPhrase(
										bold.withText(gross.toString()).build())
								.withBorder(Rectangle.TOP)
								.build());

		if (paymentConditions != null)
			footerBuilder.addCell(
					leftAlign
							.withPhrase(
									new PhraseBuilder(paymentConditions)
											.build())
							.withBorder(Rectangle.NO_BORDER)
							.build())
					.addCell(
							rightAlign
									.withPhrase(bold.withText("").build())
									.withBorder(Rectangle.NO_BORDER)
									.build());

		return footerBuilder;
	}

	public static CustomPdfPTableBuilder createInfoTable(String leftTop,
			String leftBottom,
			String rightTop, String rightBottom) {

		CustomPdfPTableBuilder infoTableBuilder = new CustomPdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithTwoCols())
				.addCell(new PdfPCellBuilder(
						new PhraseBuilder(leftTop).build()).build())
				.addCell(new PdfPCellBuilder(
						new PhraseBuilder(rightTop).build()).build())
				.addCell(new PdfPCellBuilder(
						new PhraseBuilder(leftBottom).build()).build())
				.addCell(new PdfPCellBuilder(
						new PhraseBuilder(rightBottom).build()).build());
		return infoTableBuilder;
	}

	public static CustomPdfPTableBuilder createInfoTable(
			String leftTop, String rightTop,
			String leftMiddle, String rightMiddle,
			String leftBottom, String rightBottom) {

		CustomPdfPTableBuilder infoTableBuilder = new CustomPdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithTwoCols())
				.addCell(new PdfPCellBuilder(
						new PhraseBuilder(leftTop).build()).build())
				.addCell(new PdfPCellBuilder(
						new PhraseBuilder(rightTop).build()).build())
				.addCell(new PdfPCellBuilder(
						new PhraseBuilder(leftMiddle).build()).build())
				.addCell(new PdfPCellBuilder(
						new PhraseBuilder(rightMiddle).build()).build())
				.addCell(new PdfPCellBuilder(
						new PhraseBuilder(leftBottom).build()).build())
				.addCell(new PdfPCellBuilder(
						new PhraseBuilder(rightBottom).build()).build());
		return infoTableBuilder;
	}
}
