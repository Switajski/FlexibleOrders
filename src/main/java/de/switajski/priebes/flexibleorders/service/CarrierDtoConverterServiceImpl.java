package de.switajski.priebes.flexibleorders.service;

import de.switajski.priebes.flexibleorders.domain.Carrier;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.web.dto.CarrierDto;

public class CarrierDtoConverterServiceImpl {

	public static Carrier toCarrier(CarrierDto dto, Carrier carrier) {
		Address address = new Address(dto.getName1(), dto.getName2(), dto.getStreet(), dto.getPostalCode(), 
				dto.getCity(), Country.DEUTSCHLAND);
		carrier.setAddress(address);
		carrier.setCarrierNumber(dto.getCarrierNumber());
		carrier.setId(dto.getId());
		carrier.setName(dto.getName());
		return carrier;
	}
	
}
