package de.switajski.priebes.flexibleorders.domain.embeddable;

import javax.persistence.Embeddable;

/**
 * 
 * @author Marek Switajski
 *
 */
@Embeddable
public class ContactInformation {

	private String contact1, contact2, contact3, contact4;

	public String getContact1() {
		return contact1;
	}

	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}

	public String getContact2() {
		return contact2;
	}

	public void setContact2(String contact2) {
		this.contact2 = contact2;
	}

	public String getContact3() {
		return contact3;
	}

	public void setContact3(String contact3) {
		this.contact3 = contact3;
	}

	public String getContact4() {
		return contact4;
	}

	public void setContact4(String contact4) {
		this.contact4 = contact4;
	}

}
