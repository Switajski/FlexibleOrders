package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.Date;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;

public class ExpectedDeliveryStringCreator {

	public static String createExpectedDeliveryWeekString(Date expectedDelivery) {
		String expectedDeliveryString = "";
		if (expectedDelivery != null)
			expectedDeliveryString = "voraus. Lieferwoche: "
					+ PriebesIText5PdfView.weekDateFormat.format(expectedDelivery);
		return expectedDeliveryString;
	}
	
	public static String createDeliveryWeekString(Date expectedDelivery, DeliveryHistory history) {
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
					+ PriebesIText5PdfView.weekDateFormat.format(expectedDelivery)
					+ ". KW";
		return expectedDeliveryString;
	}
}
