package de.switajski.priebes.flexibleorders.application;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.reference.Country;

public class BusinessConstants {

	public static final Address MY_ADDRESS = new Address(
			"Priebes", "OHG", "Maxstrasse 1", 71636, "Ludwigsburg", Country.DEUTSCHLAND
    );
}
