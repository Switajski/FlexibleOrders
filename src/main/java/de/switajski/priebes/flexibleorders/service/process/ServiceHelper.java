package de.switajski.priebes.flexibleorders.service.process;

import de.switajski.priebes.flexibleorders.application.QuantityCalculator;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class ServiceHelper {

	public static void validateQuantity(ItemDto entry,
			ReportItem reportItem) {
		Integer quantityToDeliver = entry.getQuantityLeft();
		if (quantityToDeliver == null)
			throw new IllegalArgumentException("Menge nicht angegeben");
		if (quantityToDeliver < 1)
			throw new IllegalArgumentException("Menge kleiner eins");
		if (quantityToDeliver > QuantityCalculator.calculate(
				reportItem))
			throw new IllegalArgumentException(
					"angeforderte Menge ist zu gross");
	}
}
