package de.switajski.priebes.flexibleorders.service.process;

import de.switajski.priebes.flexibleorders.application.QuantityCalculator;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class ServiceHelper {

    public static void validateQuantity(Integer qty, ReportItem reportItem) {
        if (qty == null) throw new IllegalArgumentException("Menge nicht angegeben");
        if (QuantityCalculator.calculateLeft(reportItem) == 0) throw new IllegalArgumentException(
                "eine angegebene position hat keine offenen Positionen mehr");
        if (qty < 1) throw new IllegalArgumentException("Menge kleiner eins");
        if (qty > QuantityCalculator.calculateLeft(reportItem)) throw new IllegalArgumentException(
                "angeforderte Menge ist zu gross");
    }
}
