package de.switajski.priebes.flexibleorders.exceptions;

public class BusinessInputException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public BusinessInputException() {
        super();
    }

    public BusinessInputException(String message) {
        super(message);
    }

    public BusinessInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessInputException(Throwable cause) {
        super(cause);
    }
}
