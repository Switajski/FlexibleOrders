package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingCostsItem;

@Service
public class ShippingCostsService {

    @Autowired
    DeliveryHistoryService dhService;
    
    @Transactional(readOnly=true)
    public Amount calculate(Report report){
        Amount a = Amount.ZERO_EURO;
        for (DeliveryNotes si:dhService.retrieveDeliveryNotesFrom(report)){
            for (ReportItem i:si.getItems())
                if (i instanceof ShippingCostsItem)
                    a = ((ShippingCostsItem) i).getCosts().add(a);
        }
        return a;
    }
    
}
