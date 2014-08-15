package de.switajski.priebes.flexibleorders.service.process;

import de.switajski.priebes.flexibleorders.application.QuantityCalculator;
import de.switajski.priebes.flexibleorders.domain.ReportItem;

public class ServiceHelper {

	public static void validateQuantity(Integer quantityToDeliver,
			ReportItem reportItem) {
		if (quantityToDeliver == null)
			throw new IllegalArgumentException("Menge nicht angegeben");
		if (quantityToDeliver < 1)
			throw new IllegalArgumentException("Menge kleiner eins");
		if (quantityToDeliver > QuantityCalculator.calculateLeft(reportItem))
			throw new IllegalArgumentException(
					"angeforderte Menge ist zu gross");
	}
}
