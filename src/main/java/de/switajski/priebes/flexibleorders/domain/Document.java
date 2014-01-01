package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Entity;

@Entity
public class Document extends GenericEntity{
	
	private String documentNumber;

	public String getDocumentNumber() {
		return documentNumber;
	}

}
