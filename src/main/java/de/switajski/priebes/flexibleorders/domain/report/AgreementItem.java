package de.switajski.priebes.flexibleorders.domain.report;

import javax.persistence.Entity;

@Entity
public class AgreementItem extends ReportItem {

	@Override
	public String provideStatus() {
		return "Auftrag";
	}

}
