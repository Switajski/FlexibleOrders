package de.switajski.priebes.flexibleorders.exceptions;

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
        super("Folgende Attribute der Kaufvertr" + Unicode.aUml + "ge sind widersprechend\n"
                + BeanUtil.createStringOfDifferingAttributes(contradictingPurchaseAgreements));
    }

}
