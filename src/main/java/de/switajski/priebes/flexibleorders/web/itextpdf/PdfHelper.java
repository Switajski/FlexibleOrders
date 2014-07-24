package de.switajski.priebes.flexibleorders.web.itextpdf;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.CustomerDetails;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPCellBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;

public class PdfHelper {

	public static void insertExtInfoTable(Document document,
			CustomerDetails details, Address shipAddr, String documentNo,
			String expectedDeliveryString, String date, String customerNo)
			throws DocumentException {
		PhraseBuilder pb = new PhraseBuilder("");
		PdfPCellBuilder cellb = new PdfPCellBuilder(new Phrase());
		
		String saleRepresentative = StringUtils.isEmpty(details.getSaleRepresentative()) ? "" : "Vertreter: " + details.getSaleRepresentative();
		String contact1 = StringUtils.isEmpty(details.getContact1()) ? "" : "Ansprechpartner: " + details.getContact1();
		String contact2 = StringUtils.isEmpty(details.getContact2()) ? "" : details.getContact2();
		String contact3 = StringUtils.isEmpty(details.getContact3()) ? "" : details.getContact3();
		String mark = StringUtils.isEmpty(details.getMark()) ? "" : "Ihr Zeichen: " + details.getMark();
		String vendorno = StringUtils.isEmpty(details.getVendorNumber()) ? "" : "Lieferantennr.: " + details.getVendorNumber();
		CustomPdfPTableBuilder infoTableBuilder = new CustomPdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithThreeCols())

				.addCell(cellb.withPhrase(
						pb.withText(date)
								.build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(customerNo).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(vendorno).build()).build())

				.addCell(cellb.withPhrase(
						pb.withText(documentNo).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText("Lieferadresse:").build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(saleRepresentative).build()).build())

				.addCell(cellb.withPhrase(
						pb.withText(expectedDeliveryString).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(" " + shipAddr.getName1() + " " + shipAddr.getName2()).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(contact1).build()).build())
		
				.addCell(cellb.withPhrase(
						pb.withText(" ").build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(" " + shipAddr.getStreet()).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(contact2).build()).build())
				
				.addCell(cellb.withPhrase(
						pb.withText(mark).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(" " + shipAddr.getPostalCode() + " " +  shipAddr.getCity()).build()).build())
				.addCell(cellb.withPhrase(
						pb.withText(contact3).build()).build());

		PdfPTable infoTable = infoTableBuilder.build();

		infoTable.setWidthPercentage(100);
		
		document.add(infoTable);
	}
	
}
