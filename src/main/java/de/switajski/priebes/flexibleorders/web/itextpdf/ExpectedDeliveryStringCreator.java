package de.switajski.priebes.flexibleorders.web.itextpdf;

import org.joda.time.LocalDate;

public class ExpectedDeliveryStringCreator {

	public static String createExpectedDeliveryWeekString(LocalDate expectedDelivery) {
		String expectedDeliveryString = "";
		if (expectedDelivery != null)
			expectedDeliveryString = "voraus. Lieferwoche: "
					+ expectedDelivery.toString("w");
		return expectedDeliveryString;
	}
	
	public static String createDeliveryWeekString(LocalDate expectedDelivery, boolean expectedDeliveryDateDeviates) {
		String description;
		if (expectedDelivery == null)
			description = "vorauss. Lieferwoche: ";
		else if (expectedDeliveryDateDeviates)
			description = "Von Bestellung abweichende Lieferwoche: ";
		else 
			description = "Lieferwoche: ";
		
		String expectedDeliveryString = "";
		if (expectedDelivery != null)
			expectedDeliveryString = description
					+ expectedDelivery.toString("w") + ". KW";
		return expectedDeliveryString;
	}
}
