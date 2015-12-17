package de.switajski.priebes.flexibleorders.reference;

/**
 * Country codes from <br/>
 * ISO 3166-1 alpha-2
 *
 * @author Marek
 *
 */
public enum Country {
    DE("Deutschland"),
    CH("Schweiz"),
    AT("Oesterreich"),
    PL("Polska"),
    FR("France"),
    IT("Italia"),
    UK("United Kingdom"),
    IE("Ireland");

    private Country(final String text) {
        this.text = text;
    }

    private final String text;

    public String getName() {
        return text;
    }

    public static Country map(String name) {
        for (Country c : Country.values()) {
            if (c.equals(name)) return c;
        }
        return null;
    }
}
