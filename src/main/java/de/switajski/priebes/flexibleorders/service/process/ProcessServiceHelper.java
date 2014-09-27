package de.switajski.priebes.flexibleorders.service.process;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.switajski.priebes.flexibleorders.application.QuantityCalculator;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.BusinessInputException;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

//TODO: bring qtyLeft methods back to domain
public class ProcessServiceHelper {

    /**
     * 
     * @param qty to bring in the next process step
     * @param reportItem should have enough 
     */
    public static void validateQuantity(Integer qty, ReportItem reportItem) {
        if (qty == null) throw new BusinessInputException("Menge nicht angegeben");
        if (QuantityCalculator.calculateLeft(reportItem) == 0) throw new BusinessInputException(
                reportItem.toString() + " hat keine offenen Positionen mehr");
        if (qty < 1) throw new BusinessInputException("Menge kleiner eins");
        if (qty > QuantityCalculator.calculateLeft(reportItem)) throw new BusinessInputException(
                "angeforderte Menge ist zu gross");
    }
    
    public static Set<Long> extractReportItemIds(List<ItemDto> agreementItemDtos) {
        Set<Long> riIds = new HashSet<Long>();
        for (ItemDto i:agreementItemDtos)
            riIds.add(i.id);
        return riIds;
    }
}
