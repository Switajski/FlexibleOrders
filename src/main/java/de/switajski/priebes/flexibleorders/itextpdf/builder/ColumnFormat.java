package de.switajski.priebes.flexibleorders.itextpdf.builder;

public class ColumnFormat {

	public String heading;
	public int alignment;
	public float relativeWidth;

	public ColumnFormat(String heading, int alignment, float relativeWidth) {
		this.heading = heading;
		this.alignment = alignment;
		this.relativeWidth = relativeWidth;
	}
}
