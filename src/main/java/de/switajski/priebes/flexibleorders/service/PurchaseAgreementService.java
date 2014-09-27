package de.switajski.priebes.flexibleorders.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.application.BeanUtil;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.exceptions.BusinessInputException;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;

@Service
public class PurchaseAgreementService {
    
    private static Logger log = Logger.getLogger(PurchaseAgreementService.class);

    @Autowired
    private ReportItemRepository reportItemRepo;
    
    public PurchaseAgreement retrieveOneOrFail(Set<Long> riIds){
        DeliveryHistory dh = new DeliveryHistory(reportItemRepo.findAll(riIds));
        if (!dh.hasEqualPurchaseAgreements()){
            throw new BusinessInputException(createErrorMessage(dh));
        }
        return dh.getPurchaseAgreements().iterator().next();
    }

    private String createErrorMessage(DeliveryHistory dh) {
        PurchaseAgreement pa1 = dh.getPurchaseAgreements().iterator().next();
        PurchaseAgreement pa2 = findDiffering(dh.getPurchaseAgreements(), pa1);
        String msg;
        String error = "Die Attribute der Kaufvertr"+Unicode.aUml+"ge %s sind widersprechend";
        try {
            msg = String.format(error, BeanUtil.getDifferencesOfObjects(pa1, pa2).toString());
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.warn("Could not compare two purchaseAgreements:", e);
            msg = error;
        }
        return msg;
    }

    private PurchaseAgreement findDiffering(Set<PurchaseAgreement> purchaseAgreements, PurchaseAgreement pa) {
        for (PurchaseAgreement pa2:purchaseAgreements)
            if (!pa2.equals(pa))
                return pa2;
        throw new IllegalArgumentException("No differing found");
    }

}
