package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;

public class ConfirmationItemBuilder extends
		ReportItemBuilder<ConfirmationItemBuilder> implements
		Builder<ConfirmationItem> {

	@Override
	public ConfirmationItem build() {
		ConfirmationItem ii = new ConfirmationItem(
				(ConfirmationReport) report,
				type,
				item,
				quantity,
				date);
		ii.setQuantity(quantity);
		ii.setReport(report);
		return ii;
	}
}
