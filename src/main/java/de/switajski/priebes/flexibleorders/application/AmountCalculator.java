package de.switajski.priebes.flexibleorders.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.reference.Currency;

public class AmountCalculator {

	public static Amount sum(Collection<Amount> amounts) {
		Amount sum = Amount.ZERO_EURO;
		Currency currency = null;
		for (Amount a : amounts) {
			if (currency == null)
				currency = a.getCurrency();
			else if (a.getCurrency() != currency)
				throw new IllegalArgumentException(
						"Trying to sum Amounts with different currencies");
			sum = a.add(sum);
		}
		return sum;
	}

	public static List<Amount> getAmountsTimesQuantity(Collection<ReportItem> reportItems) {
		List<Amount> amounts = new ArrayList<Amount>();
		for (ReportItem ri : reportItems) {
			Amount priceNet = ri.getOrderItem().getNegotiatedPriceNet();
			if (priceNet == null)
				throw new IllegalArgumentException(
						"Report has items with no price set");
			else
				amounts.add(priceNet.multiply(ri.getQuantity()));
		}
		return amounts;
	}

	public static List<Amount> getAmountsTimesQuantity2(Collection<OrderItem> orderItems) {
		List<Amount> amounts = new ArrayList<Amount>();
		for (OrderItem ri : orderItems) {
			Amount priceNet = ri.getNegotiatedPriceNet();
			if (priceNet == null)
				throw new IllegalArgumentException(
						"Order has items with no price set");
			else
				amounts.add(priceNet.multiply(ri.getOrderedQuantity()));
		}
		return amounts;
	}
	
}
