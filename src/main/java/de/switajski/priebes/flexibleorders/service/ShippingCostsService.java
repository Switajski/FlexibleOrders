package de.switajski.priebes.flexibleorders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Report;

@Service
public class ShippingCostsService {

    @Autowired
    DeliveryHistoryService dhService;
    
    @Transactional(readOnly=true)
    public Amount calculate(Report report){
        Amount a = Amount.ZERO_EURO;
        for (DeliveryNotes si:dhService.retrieveDeliveryNotesFrom(report)){
            a = si.getShippingCosts().add(a);
        }
        return a;
    }
    
}
