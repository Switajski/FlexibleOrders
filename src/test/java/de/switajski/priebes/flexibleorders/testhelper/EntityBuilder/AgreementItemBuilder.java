package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.AgreementItem;
import de.switajski.priebes.flexibleorders.domain.OrderAgreement;

public class AgreementItemBuilder extends ReportItemBuilder<AgreementItem, Builder<AgreementItem>>{

	public AgreementItem build() {
		AgreementItem ii = new AgreementItem();
		super.build(ii);
		ii.setQuantity(quantity);
		ii.setReport((OrderAgreement) report);
		return ii;
	}
}
