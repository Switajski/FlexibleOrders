package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.web.FilterDispatcher;

@Service
public class StatisticsService {

	@Autowired
	private ReportItemRepository reportItemRepo;
	
	public Amount calculateOpenAmount(String state){
		List<ReportItem> ris = reportItemRepo.findAll(FilterDispatcher.dispatchStatus(state));
		Amount summed = Amount.ZERO_EURO;
		for (ReportItem ri:ris){
			summed = summed.add(ri.getOrderItem().getNegotiatedPriceNet()
					.multiply(ri.getQuantity() /* TODO - QuantityLeftCalculator.calculate*/));
		}
		return summed;
	}
}
