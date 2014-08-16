package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;

public class ConfirmationItemBuilder extends ReportItemBuilder<ConfirmationItem, Builder<ConfirmationItem>>{

	@Override
	public ConfirmationItem build() {
		ConfirmationItem ii = new ConfirmationItem();
		super.build(ii);
		return ii;
	}
}
