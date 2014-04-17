package de.switajski.priebes.flexibleorders.itextpdf.builder;

public class TableProperties {

	public String heading;
	public int alignment;
	public float relativeWidth;

	public TableProperties(String heading, int alignment, float relativeWidth) {
		this.heading = heading;
		this.alignment = alignment;
		this.relativeWidth = relativeWidth;
	}
}
