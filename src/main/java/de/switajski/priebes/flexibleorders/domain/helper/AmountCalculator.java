package de.switajski.priebes.flexibleorders.domain.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.Currency;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.exception.BusinessException;

public class AmountCalculator {
	
	public static Amount sum(Collection<Amount> amounts){
		Amount sum = Amount.ZERO_EURO;
		Currency currency = null;
		for (Amount a:amounts){
			if (currency == null)
				currency = a.getCurrency();
			else if (a.getCurrency() != currency)
				throw new BusinessException("Trying to sum Amounts with different currencies");
			sum = a.add(sum);
		}
		return sum;
	}
	
	public static List<Amount> getAmountsTimesQuantity(Report report){
		List<Amount> amounts = new ArrayList<Amount>();
		for (ReportItem ri:report.getItems()){
				Amount priceNet = ri.getOrderItem().getNegotiatedPriceNet();
				if (priceNet == null)
					throw new BusinessException("Report has items with no price set");
				else amounts.add(priceNet.multiply(ri.getQuantity()));
		}
		return amounts;
	}
	
	public static List<Amount> getAmountsTimesQuantity(Order report){
		List<Amount> amounts = new ArrayList<Amount>();
		for (OrderItem ri:report.getItems()){
				Amount priceNet = ri.getNegotiatedPriceNet();
				if (priceNet == null)
					throw new BusinessException("Order has items with no price set");
				else amounts.add(priceNet.multiply(ri.getOrderedQuantity()));
		}
		return amounts;
	}
	
}
