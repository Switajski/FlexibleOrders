package de.switajski.priebes.flexibleorders.exceptions;

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

}
