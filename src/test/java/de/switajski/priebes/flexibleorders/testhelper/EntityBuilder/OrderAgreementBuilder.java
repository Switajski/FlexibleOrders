package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.report.OrderAgreement;

public class OrderAgreementBuilder extends ReportBuilder<OrderAgreement, Builder<OrderAgreement>> {

	@Override
	public OrderAgreement build() {
		OrderAgreement oa = new OrderAgreement();
		super.build(oa);
		return oa;
	}

}
