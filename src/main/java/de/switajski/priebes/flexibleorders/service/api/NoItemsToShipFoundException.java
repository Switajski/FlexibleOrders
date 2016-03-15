package de.switajski.priebes.flexibleorders.service.api;

public class NoItemsToShipFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -3158216826125835032L;

    public NoItemsToShipFoundException() {
        super("Konnte zu gegebener Position keinen faellige Position zum Ausliefern finden");
    }
}
