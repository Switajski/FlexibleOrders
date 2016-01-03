package de.switajski.priebes.flexibleorders.service;

import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.validation.UniqueDocumentNumber;

public class QuantityUtility {

    public static Integer sumQty(Set<? extends ReportItem> reportItems) {
        int summed = 0;
        for (ReportItem ri : reportItems) {
            summed = summed + ri.getQuantity();
        }
        return summed;
    }

    // TODO: move to validator
    /**
     * @deprecated use custom validator like @link {@link UniqueDocumentNumber}
     * @param qty
     *            to bring in the next process step
     * @param reportItem
     *            should have enough
     */
    public static void validateQuantity(Integer qty, ReportItem reportItem) {
        if (qty == null) throw new IllegalArgumentException("Menge nicht angegeben");

        if (qty < 1) throw new IllegalArgumentException("Menge kleiner eins");

        int toBeProcessed = reportItem.overdue();
        if (toBeProcessed == 0) throw new IllegalArgumentException(reportItem.toString() +
                " hat keine offenen Positionen mehr");

        if (qty > toBeProcessed) {
            StringBuilder msg = new StringBuilder("angeforderte Menge ist ").append(qty)
                    .append(". ")
                    .append(reportItem.overdue())
                    .append(" sind aber noch ")
                    .append(Unicode.U_UML)
                    .append("brig.");
            throw new IllegalArgumentException(msg.toString());
        }
    }
}
