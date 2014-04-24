package de.switajski.priebes.flexibleorders.reference;

public enum Country {
	DEUTSCHLAND("Deutschland"),
	SCHWEITZ("Schweitz"),
	OESTERREICH("Oesterreich"),
	POLSKA("Polska"),
	FRANCE("France"),
	ITALIA("Italia"),
	UNITED_KINGDOM("United Kingdom"),
	IRELAND("Ireland");

	private Country(final String text) {
		this.text = text;
	}

	private final String text;

	@Override
	public String toString() {
		return text;
	}
}
