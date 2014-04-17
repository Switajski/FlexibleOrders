package de.switajski.priebes.flexibleorders.json;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Helper Class for ExtJs Json Filter Deserialization
 * 
 * @author Marek
 * 
 */
@JsonAutoDetect
public class JsonFilter {

	public String field;
	public String value;
	public String type;

	public String getField() {
		return field;
	}

	public String getValue() {
		return value;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

}
