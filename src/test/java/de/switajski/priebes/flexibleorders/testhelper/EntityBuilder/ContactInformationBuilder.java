package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.embeddable.ContactInformation;

public class ContactInformationBuilder implements Builder<ContactInformation>{

	private String contact1, contact2, contact3, contact4;
	
	@Override
	public ContactInformation build() {
		ContactInformation c = new ContactInformation();
		c.setContact1(contact1);
		c.setContact2(contact2);
		c.setContact3(contact3);
		c.setContact4(contact4);
		return c;
	}

	public ContactInformationBuilder setContact1(String contact1) {
		this.contact1 = contact1;
		return this;
	}

	public ContactInformationBuilder setContact2(String contact2) {
		this.contact2 = contact2;
		return this;
	}

	public ContactInformationBuilder setContact3(String contact3) {
		this.contact3 = contact3;
		return this;
	}

	public ContactInformationBuilder setContact4(String contact4) {
		this.contact4 = contact4;
		return this;
	}

}
