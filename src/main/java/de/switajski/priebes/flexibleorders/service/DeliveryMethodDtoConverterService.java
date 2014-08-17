package de.switajski.priebes.flexibleorders.service;

import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.web.dto.DeliveryMethodDto;

@Service
public class DeliveryMethodDtoConverterService {

	public DeliveryMethod toDeliveryMethod(DeliveryMethodDto dto,
			DeliveryMethod deliveryMethod) {
			Address address = new Address(
					dto.getName1(),
					dto.getName2(),
					dto.getStreet(),
					dto.getPostalCode(),
					dto.getCity(),
					Country.DEUTSCHLAND);
			deliveryMethod.setAddress(address);
			deliveryMethod.setId(dto.getId());
			deliveryMethod.setName(dto.getName());
			return deliveryMethod;
	}

}
