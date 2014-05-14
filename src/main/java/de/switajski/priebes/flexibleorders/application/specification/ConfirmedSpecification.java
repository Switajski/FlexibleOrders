package de.switajski.priebes.flexibleorders.application.specification;

import de.switajski.priebes.flexibleorders.application.QuantityLeftCalculator;
import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;

/**
 * Defines and validates an item in a confirmed state.</br>
 *   
 * @author Marek Switajski
 *
 */
public class ConfirmedSpecification extends ItemSpecification{

	public boolean isSatisfiedBy(final OrderItem item){
		//TODO: take account to executed tasks (includeTaskExecuted) 
		if (item.getReportItems().isEmpty()) return false;
		if (!item.getDeliveryHistory().getCancellationItems().isEmpty()) return false;
		for (ReportItem he: item.getDeliveryHistory().getConfirmationItems()){
			if (he.getReport() == null || ((ConfirmationReport) he.getReport()).getInvoiceAddress() == null ||
					//TODO: check VAT_RATE
					item.getNegotiatedPriceNet() == null ||
					item.getOrder().getCustomer() == null) 
				return false;
		}
		if (new QuantityLeftCalculator().toBeShipped(item.getDeliveryHistory()) == 0)
			return true;
		return false;
	}
	
}
