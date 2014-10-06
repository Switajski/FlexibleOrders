package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryPurchaseAgreementException;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.service.CustomerDetailsService;
import de.switajski.priebes.flexibleorders.service.ShippingAddressService;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;

@Service
public class DeliveryNotesToDtoConversionService {

	@Autowired
	ReportToDtoConversionService reportToDtoConversionService;
	@Autowired
	ShippingAddressService shippingAddressService;
	@Autowired
	CustomerDetailsService customerDetailsService;
	
	@Transactional(readOnly=true)
	public ReportDto toDto(DeliveryNotes report){
			ReportDto dto = reportToDtoConversionService.toDto(report);
			
			Set<Address> shippingAddresses = shippingAddressService.retrieve(report.getItems());
			if (shippingAddresses.isEmpty())
				throw new NotFoundException("No shipping address for DeliveryNotes found");
			else if (shippingAddresses.size() > 1)
				throw new ContradictoryPurchaseAgreementException("Found more than one shipping address for one delivery notes");
			dto.shippedAddress = shippingAddresses.iterator().next();
			
			dto.trackNumber = report.getTrackNumber();
			dto.shippingCosts = report.getShippingCosts();
			
			Set<CustomerDetails> customerDetailss = customerDetailsService.retrieve(report.getItems());
			if (customerDetailss.isEmpty())
				dto.customerDetails = null;
			else if (customerDetailss.size() > 1)
				throw new ContradictoryPurchaseAgreementException("Found contradictory information about customer");
			else dto.customerDetails = customerDetailss.iterator().next();
				
			return dto;
	}
	
}
