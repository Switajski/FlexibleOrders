package de.switajski.priebes.flexibleorders.web.itextpdf;

import org.joda.time.LocalDate;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;

public class ExpectedDeliveryStringCreator {

	public static String createExpectedDeliveryWeekString(LocalDate expectedDelivery) {
		String expectedDeliveryString = "";
		if (expectedDelivery != null)
			expectedDeliveryString = "voraus. Lieferwoche: "
					+ PriebesIText5PdfView.weekDateFormat.format(expectedDelivery);
		return expectedDeliveryString;
	}
	
	public static String createDeliveryWeekString(LocalDate expectedDelivery, DeliveryHistory history) {
		String description;
		Boolean hasDifferentDds = history.hasDifferentDeliveryDates(); 
		if (hasDifferentDds == null)
			description = "vorauss. Lieferwoche: ";
		else if (hasDifferentDds)
			description = "Von Bestellung abweichende Lieferwoche: ";
		else 
			description = "Lieferwoche: ";
		
		String expectedDeliveryString = "";
		if (expectedDelivery != null)
			expectedDeliveryString = description
					+ PriebesIText5PdfView.weekDateFormat.format(expectedDelivery.toDateTimeAtStartOfDay().toDate())
					+ ". KW";
		return expectedDeliveryString;
	}
}
