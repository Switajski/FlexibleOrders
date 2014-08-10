package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Entity;

@Entity
public class AgreementItem extends ReportItem {

	@Override
	public String provideStatus() {
		return "Auftrag";
	}

}
