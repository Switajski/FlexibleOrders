package de.switajski.priebes.flexibleorders.exceptions;

public class ContradictingPurchaseAgreementException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public ContradictingPurchaseAgreementException() {
        super();
    }

    public ContradictingPurchaseAgreementException(String message) {
        super(message);
    }

    public ContradictingPurchaseAgreementException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContradictingPurchaseAgreementException(Throwable cause) {
        super(cause);
    }
}
