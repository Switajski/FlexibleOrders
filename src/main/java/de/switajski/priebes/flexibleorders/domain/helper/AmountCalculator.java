package de.switajski.priebes.flexibleorders.domain.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.exception.BusinessException;

public class AmountCalculator {
	
	private List<Amount> netAmounts = new ArrayList<Amount>();
	
	/**
	 * 
	 * @param vat in Germany 0.19
	 */
	public AmountCalculator() {}
	
	public AmountCalculator addNet(Amount netAmount){
		netAmounts.add(netAmount);
		return this;
	}
	
	public AmountCalculator addGross(Amount grossAmount, Double vat){
		netAmounts.add(grossAmount.devide(vat+1));
		return this;
	}
	
	public Amount calculateNetAmount(){
		return sum();
	}

	private Amount sum() {
		if (netAmounts.isEmpty())
			throw new IllegalStateException("Nothing added to calculate");
		Amount net = new Amount(BigDecimal.ZERO, netAmounts.get(0).getCurrency());
		for (Amount a:netAmounts){
			if (!a.getCurrency().equals(net.getCurrency()))
				throw new IllegalStateException("Amounts to be calculated have various currencies");
			net = net.add(a);
		}
		return net;
	}
	
	public Amount calculateVatAmount(Double vatRate){
		Amount netSummed = sum();
		return new Amount(
			netSummed.getValue().multiply(new BigDecimal(vatRate)),
			netSummed.getCurrency()
		);
	}
	
	/**
	 * Convenience method for calculating net amount of an {@link Order} directly
	 * @param order
	 * @return
	 */
	public static Amount calculateNetAmount(Order order){
		AmountCalculator calc = sum(order);
		return calc.calculateNetAmount();
	}
	
	/**
	 * Convenience method for calculating VAT amount of an {@link Order} directly
	 * @param order
	 * @return
	 */
	public static Amount calculateVatAmount(Order order, Double vatRate){
		AmountCalculator calc = AmountCalculator.sum(order);
		return calc.calculateVatAmount(vatRate);
	}

	private static AmountCalculator sum(Order order) {
		AmountCalculator calc = new AmountCalculator();
		for (OrderItem oi: order.getItems()){
			if (oi.getNegotiatedPriceNet() == null || oi.getNegotiatedPriceNet().getValue() == null) 
				throw new BusinessException("Order has items with no price set");
			else calc.addNet(oi.getNegotiatedPriceNet().multiply(oi.getOrderedQuantity()));
		}
		return calc;
	}
	
	/**
	 * Convenience method for calculating net amount of an {@link Report} directly
	 * @param report
	 * @return
	 */
	public static Amount calculateNetAmount(Report report){
		AmountCalculator calc = sum(report);
		return calc.calculateNetAmount();
	}
	
	/**
	 * Convenience method for calculating VAT amount of an {@link Report} directly
	 * @param report
	 * @return
	 */
	public static Amount calculateVatAmount(Report report, Double vatRate){
		AmountCalculator calc = AmountCalculator.sum(report);
		return calc.calculateVatAmount(vatRate);
	}
	
	private static AmountCalculator sum(Report report) {
		AmountCalculator calc = new AmountCalculator();
		for (ReportItem oi: report.getItems()){
			if (!oi.getOrderItem().isShippingCosts()){
				Amount priceNet = oi.getOrderItem().getNegotiatedPriceNet();
				if (priceNet == null)
					throw new BusinessException("Report has items with no price set");
				else calc.addNet(priceNet.multiply(oi.getQuantity()));
			}
		}
		return calc;
	}
	
}
