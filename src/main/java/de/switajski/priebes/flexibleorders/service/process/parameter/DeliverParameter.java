package de.switajski.priebes.flexibleorders.service.process.parameter;

import java.util.Date;
import java.util.List;

import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class DeliverParameter {
	public String deliveryNotesNumber;
	public Long customerNumber;
	public String trackNumber;
	public String packageNumber;
	public Amount shipment;
	public Date created;
	public List<ItemDto> agreementItemDtos;

	public DeliverParameter(String deliveryNotesNumber, String trackNumber,
			String packageNumber, Amount shipment, Date created,
			List<ItemDto> agreementItemDtos) {
		this.deliveryNotesNumber = deliveryNotesNumber;
		this.trackNumber = trackNumber;
		this.packageNumber = packageNumber;
		this.shipment = shipment;
		this.created = created;
		this.agreementItemDtos = agreementItemDtos;
	}

    public DeliverParameter() {
    }
}