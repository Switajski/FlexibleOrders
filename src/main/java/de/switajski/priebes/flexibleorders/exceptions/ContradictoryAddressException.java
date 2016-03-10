package de.switajski.priebes.flexibleorders.exceptions;

public class ContradictoryAddressException extends Exception {

    private static final long serialVersionUID = 1L;

    public ContradictoryAddressException() {
        super();
    }

    public ContradictoryAddressException(String message) {
        super(message);
    }

    public ContradictoryAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContradictoryAddressException(Throwable cause) {
        super(cause);
    }

}
