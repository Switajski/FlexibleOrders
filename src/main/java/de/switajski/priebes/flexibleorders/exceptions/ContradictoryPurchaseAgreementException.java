package de.switajski.priebes.flexibleorders.exceptions;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import de.switajski.priebes.flexibleorders.application.BeanUtil;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;

public class ContradictoryPurchaseAgreementException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public ContradictoryPurchaseAgreementException() {
        super();
    }

    public ContradictoryPurchaseAgreementException(String message) {
        super(message);
    }

    public ContradictoryPurchaseAgreementException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContradictoryPurchaseAgreementException(Throwable cause) {
        super(cause);
    }
    
    public ContradictoryPurchaseAgreementException(Collection<PurchaseAgreement> contradictingPurchaseAgreements) {
        super(createErrorMessage(contradictingPurchaseAgreements));
    }
    
    private static String createErrorMessage(Collection<PurchaseAgreement> pas) {
        PurchaseAgreement pa1 = pas.iterator().next();
        PurchaseAgreement pa2 = findDiffering(pas, pa1);
        String msg;
        String error = "Die Attribute der Kaufvertr"+Unicode.aUml+"ge %s sind widersprechend";
        try {
            msg = String.format(error, BeanUtil.getDifferencesOfObjects(pa1, pa2).toString());
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Error occured, when creating ContradictoryPurchaseAgreementException", e);
        }
        return msg;
    }

    private static PurchaseAgreement findDiffering(Collection<PurchaseAgreement> pas, PurchaseAgreement pa) {
        for (PurchaseAgreement pa2:pas)
            if (!pa2.equals(pa))
                return pa2;
        throw new IllegalArgumentException("No differing found");
    }

}
