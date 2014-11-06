package de.switajski.priebes.flexibleorders.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.service.helper.StatusFilterDispatcher;
import de.switajski.priebes.flexibleorders.web.helper.ProductionState;

@Service
public class StatisticsService {

	@Autowired
	private ReportItemRepository reportItemRepo;
	
	@Autowired
	private StatusFilterDispatcher dispatcher;

	public Amount calculateOpenAmount(String state) {
		QuantityLeftCalculatorService calculator = new QuantityLeftCalculatorService();
		Set<OrderItem> calculated = new HashSet<OrderItem>();
		Amount summed = Amount.ZERO_EURO;

		List<ReportItem> ris = reportItemRepo
				.findAll(dispatcher.dispatchStatus(ProductionState.mapFromString(state)));

		for (ReportItem ri : ris) {
			if (calculated.add(ri.getOrderItem())){
				summed = summed.add(ri.getOrderItem().getNegotiatedPriceNet()
						.multiply(calculator.calculateLeft(ri)));
			}
		}
		return summed;
	}
}
