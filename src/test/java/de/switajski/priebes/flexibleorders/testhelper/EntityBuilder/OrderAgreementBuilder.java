package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.embeddable.AgreementDetails;
import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;

public class OrderAgreementBuilder extends ReportBuilder<OrderAgreement, Builder<OrderAgreement>> {

    private AgreementDetails agreementDetails;
    
	@Override
	public OrderAgreement build() {
		OrderAgreement oa = new OrderAgreement();
		super.build(oa);
		oa.setAgreementDetails(agreementDetails);
		return oa;
	}

    public OrderAgreementBuilder setAgreementDetails(AgreementDetails agreementDetails) {
        this.agreementDetails = agreementDetails;
        return this;
    }

}
