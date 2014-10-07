package de.switajski.priebes.flexibleorders.service;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

@Service
public class ExpectedDeliveryService {

	@Autowired
	PurchaseAgreementService purchaseAgreementService;
	
	public Set<LocalDate> retrieve(Set<ReportItem> items) {
		Set<LocalDate> expectedDeliveryDates = new HashSet<LocalDate>();
		for (PurchaseAgreement pa : purchaseAgreementService.retrieve(items)){
			expectedDeliveryDates.add(pa.getExpectedDelivery());
		}
		return expectedDeliveryDates;
	}

}
