package de.switajski.priebes.flexibleorders.itextpdf;

import java.time.LocalDate;

import de.switajski.priebes.flexibleorders.application.DateUtils;

public class ExpectedDeliveryStringCreator {

    public static String createExpectedDeliveryWeekString(LocalDate expectedDelivery) {
        String expectedDeliveryString = "";
        if (expectedDelivery != null) expectedDeliveryString = "voraus. Lieferwoche: "
                + DateUtils.weekOf(expectedDelivery);
        return expectedDeliveryString;
    }

    public static String createDeliveryWeekString(LocalDate expectedDelivery, boolean expectedDeliveryDateDeviates) {
        // FIXME:very buggy - and the first phrse should be bold. But isn't
        String description;
        if (expectedDelivery == null) description = "vorauss. Lieferwoche: ";
        else if (expectedDeliveryDateDeviates) description = "Von Bestellung abweichende Lieferwoche: ";
        else description = "Lieferwoche: ";

        String expectedDeliveryString = "";
        if (expectedDelivery != null) expectedDeliveryString = description
                + DateUtils.weekOf(expectedDelivery) + ". KW";
        return expectedDeliveryString;
    }
}
