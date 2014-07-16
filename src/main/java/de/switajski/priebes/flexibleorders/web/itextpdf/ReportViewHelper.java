package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;

public class ReportViewHelper {

	public static List<Paragraph> insertAddress(Address adresse)
			throws DocumentException {
		List<Paragraph> document = new ArrayList<Paragraph>();

		document.add(ParagraphBuilder.createEmptyLine());
		document.add(ParagraphBuilder.createEmptyLine());
		document.add(ParagraphBuilder.createEmptyLine());
		document.add(ParagraphBuilder.createEmptyLine());
		if (adresse == null) {
			document.add(ParagraphBuilder.createEmptyLine());
			document.add(ParagraphBuilder.createEmptyLine());
			document.add(ParagraphBuilder.createEmptyLine());
		} else {
			document.add(new ParagraphBuilder(adresse.getName1())
					.withIndentationLeft(36f)
					.withLineSpacing(12f)
					.addTextLine(adresse.getName2())
					.addTextLine(adresse.getStreet())
					.addTextLine(
							adresse.getPostalCode() + " " + adresse.getCity())
					.addTextLine(adresse.getCountry().toString())
					.build());
		}
		document.add(ParagraphBuilder.createEmptyLine());
		document.add(ParagraphBuilder.createEmptyLine());
		return document;
	}

	public static PdfPTable insertInfoTable(String rightTop,
			String rightBottom, String leftTop, String leftBottom)
			throws DocumentException {

		CustomPdfPTableBuilder infoTableBuilder = CustomPdfPTableBuilder
				.createInfoTable(
						leftTop, leftBottom, rightTop, rightBottom);
		PdfPTable infoTable = infoTableBuilder.build();
		infoTable.setWidthPercentage(100);

		return infoTable;
	}

	public static List<Paragraph> insertHeading(String heading)
			throws DocumentException {
		List<Paragraph> document = new ArrayList<Paragraph>();
		document.add(new ParagraphBuilder(heading)
				.withFont(
						FontFactory.getFont(
								PriebesIText5PdfView.FONT,
								12,
								Font.BOLD))
				.build());
		document.add(ParagraphBuilder.createEmptyLine());

		return document;
	}
}
