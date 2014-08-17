package de.switajski.priebes.flexibleorders.web.itextpdf;

import static com.itextpdf.text.Chunk.NEWLINE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.AgreementDetails;
import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPCellBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PhraseBuilder;
import de.switajski.priebes.flexibleorders.reference.DeliveryType;

public class PdfHelper {

	public static PdfPTable insertExtInfoTable(CustomerDetails details, String expectedDelivery, AgreementDetails agreeDet,
			String date, String customerNo, Collection<String> orderNumbers) {
		PhraseBuilder pb = new PhraseBuilder("");
		PdfPCellBuilder cellb = new PdfPCellBuilder(new Phrase());
		
		String shipAddress = createString(agreeDet.getShippingAddress()); 

		Phrase firstCol = new PhraseBuilder()
		.append(date)
		.append(NEWLINE + createString(orderNumbers))
		.append(NEWLINE).build();
		firstCol.add(
			new PhraseBuilder()
				.append(NEWLINE + "" + expectedDelivery)
				.append(NEWLINE)
				.append(NEWLINE).append(isEmpty(details.getMark()) ? "" : "Ihr Zeichen: " + NEWLINE + details.getMark())
				.append(StringUtils.isEmpty(details.getSaleRepresentative()) ? "" : NEWLINE + "" + NEWLINE + "Vertreter: " + NEWLINE + details.getSaleRepresentative())
				.build()
			);
		
		String sdCol = new StringBuilder()
		.append("Lieferadresse:")
		.append(NEWLINE + shipAddress)
		.append(NEWLINE)
		.append(NEWLINE)
		.append(createString(agreeDet.getDeliveryMethod()))
		.toString();
		
		String rdCol = new StringBuilder()
		.append(isEmpty(details.getVendorNumber()) ? "" : "Lieferantennr.: " + details.getVendorNumber() + NEWLINE + NEWLINE)
		.append(createString(details.getContactInformation()))
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
	
	private static String createString(DeliveryMethod deliveryMethod) {
		if (deliveryMethod.getDeliveryType() == DeliveryType.SPEDITION){
			return new StringBuilder()
			.append("Lieferart per Spedition:")
			.append(NEWLINE + createString(deliveryMethod.getAddress())).toString();
		}
		else if (deliveryMethod.getDeliveryType() == DeliveryType.POST){
			return new StringBuilder()
			.append("Lieferart per Post").toString();
		}
		return "";
	}

	private static String createString(Collection<String> orderNumbers) {
		StringBuilder oNos;
		if (orderNumbers.size() > 1)
			oNos = new StringBuilder("Bestellnummern: " + NEWLINE);
		else oNos = new StringBuilder("Bestellnr.:   ");
		
		Iterator<String> itr = orderNumbers.iterator();
		while (itr.hasNext()){
			oNos.append(itr.next());
			if (itr.hasNext())
				oNos.append(", ");
		}
		
		return oNos.toString();
	}

	private static String createString(Address a) {
		StringBuilder cib = new StringBuilder();
		if (a != null){
			if (!isEmpty(a.getName1()))
				cib.append(a.getName1());
			if (!isEmpty(a.getName2()))
				cib.append(a.getName2());
			
			if (!isEmpty(a.getStreet()))
				cib.append(NEWLINE + a.getStreet());
			
			cib.append(NEWLINE + a.getPostalCode().toString() + " ");
			cib.append(a.getCity());
			
			if (!isEmpty(a.getCountry().toString()))
				cib.append(NEWLINE).append(a.getCountry());
		}
		return cib.toString();
	}
	
	private static String createString(ContactInformation info) {
		StringBuilder cib = new StringBuilder();  
		if (info != null && !isEmpty(info.toString())){
			cib.append("Ihr Ansprechpartner:");
			if (!isEmpty(info.getContact1())) 
				cib.append(NEWLINE).append(info.getContact1());
			if (!isEmpty(info.getContact2())) 
				cib.append(NEWLINE).append(info.getContact2());
			if (!isEmpty(info.getContact3())) 
				cib.append(NEWLINE).append(info.getContact3());
			if (!isEmpty(info.getContact4())) 
				cib.append(NEWLINE).append(info.getContact4());
		}
		String cinfo = cib.toString();
		return cinfo;
	}
	
}
