package de.switajski.priebes.flexibleorders.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Helper Class for ExtJs Json Query Filter Deserialization - Json for adding a
 * filter: </br> <code>
 * 	[{"property":"orderNumber","value":1}]
 * </code>
 * 
 * @author Marek
 * 
 */
@JsonAutoDetect
public class JsonQueryFilter {

	public String property;
	public String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getProperty() {
		return this.property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}
