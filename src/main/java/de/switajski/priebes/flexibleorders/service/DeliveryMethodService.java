package de.switajski.priebes.flexibleorders.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

@Service
public class DeliveryMethodService {

	@Autowired
	PurchaseAgreementService purchaseAgreementService;
	
	@Transactional(readOnly=true)
	public Set<DeliveryMethod> retrieve(Set<ReportItem> reportItems){
		Set<DeliveryMethod> deliveryMethods = new HashSet<DeliveryMethod>();
		for (PurchaseAgreement pa:purchaseAgreementService.retrieve(reportItems))
			deliveryMethods.add(pa.getDeliveryMethod());
		return deliveryMethods;
	}
}
