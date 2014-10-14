package de.switajski.priebes.flexibleorders.service.conversion;

import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.CatalogDeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.web.dto.DeliveryMethodDto;

@Service
public class DeliveryMethodDtoConverterService {

	public CatalogDeliveryMethod toDeliveryMethod(DeliveryMethodDto dto,
			CatalogDeliveryMethod catalogDeliveryMethod) {
	    DeliveryMethod dm = catalogDeliveryMethod.getDeliveryMethod();
			Address address = new Address(
					dto.getName1(),
					dto.getName2(),
					dto.getStreet(),
					dto.getPostalCode(),
					dto.getCity(),
					Country.DEUTSCHLAND);
			dm.setAddress(address);
			dm.setId(dto.getId());
			dm.setName(dto.getName());
			return catalogDeliveryMethod;
	}

}
