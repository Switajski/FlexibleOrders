package de.switajski.priebes.flexibleorders.web.dto;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class Child {

	String text;
	boolean leaf;
	private List<Child> children = new ArrayList<Child>();

	public Child(String text, boolean leaf) {
		this.text = text;
		this.leaf = leaf;
	}

	public void addChild(Child child) {
		this.children.add(child);
	}

	public String getText() {
		return text;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public List<Child> getChildren() {
		return children;
	}
}
