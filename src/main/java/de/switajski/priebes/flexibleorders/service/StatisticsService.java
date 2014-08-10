package de.switajski.priebes.flexibleorders.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.application.QuantityCalculator;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.service.helper.ReportItemFilterDispatcher;

@Service
public class StatisticsService {

	@Autowired
	private ReportItemRepository reportItemRepo;

	public Amount calculateOpenAmount(String state) {
		QuantityCalculator calculator = new QuantityCalculator();
		Set<OrderItem> calculated = new HashSet<OrderItem>();
		Amount summed = Amount.ZERO_EURO;

		List<ReportItem> ris = reportItemRepo
				.findAll(ReportItemFilterDispatcher.dispatchStatus(state));

		for (ReportItem ri : ris) {
			if (calculated.add(ri.getOrderItem())){
				summed = summed.add(ri.getOrderItem().getNegotiatedPriceNet()
						.multiply(calculator.calculate(ri)));
			}
		}
		return summed;
	}
}
