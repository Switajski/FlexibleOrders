package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;

/**
 * @author Marek Switajski
 *
 */
public class OrderConfirmationBuilder extends ReportBuilder<OrderConfirmation, Builder<OrderConfirmation>>{

	@Override
	public OrderConfirmation build() {
		OrderConfirmation dn = new OrderConfirmation();
		super.build(dn);
		return dn;
	}
	
	public OrderConfirmationBuilder withAB11(){
		setDocumentNumber("AB11");
		return this;
	}
	
}
