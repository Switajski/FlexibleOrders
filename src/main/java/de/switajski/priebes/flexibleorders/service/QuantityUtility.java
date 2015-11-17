package de.switajski.priebes.flexibleorders.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class QuantityUtility {

    public static Integer sumQty(Set<? extends ReportItem> reportItems) {
        int summed = 0;
        for (ReportItem ri : reportItems) {
            summed = summed + ri.getQuantity();
        }
        return summed;
    }

    /**
     * 
     * @param qty to bring in the next process step
     * @param reportItem should have enough 
     */
    public void validateQuantity(Integer qty, ReportItem reportItem) {
        if (qty == null) 
        	throw new IllegalArgumentException("Menge nicht angegeben");
        
        if (reportItem.toBeProcessed() == 0) 
        	throw new IllegalArgumentException(reportItem.toString() + " hat keine offenen Positionen mehr");
        
        if (qty < 1) 
        	throw new IllegalArgumentException("Menge kleiner eins");
        
        if (qty > reportItem.toBeProcessed()) 
        	throw new IllegalArgumentException("angeforderte Menge ist zu gross");
    }
}
