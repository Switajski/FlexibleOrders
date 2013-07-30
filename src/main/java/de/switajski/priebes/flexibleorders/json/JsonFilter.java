package de.switajski.priebes.flexibleorders.json;
/**
 * Helper Class for ExtJs Json Filter Deserialization
 * @author Marek
 *
 */
public class JsonFilter {

	public final String type;
	public final String field;
	public final String value;

	public JsonFilter(String fieldName, String value, String type) {
		this.field = fieldName;
		this.value = value;
		this.type = type;
	}
}
